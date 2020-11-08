package auth

import (
    "fmt"
    "time"
    "encoding/json"
    "github.com/dgrijalva/jwt-go"
    "net/http"
    "log"
    "math/rand"
	db "../db"
)

var jwtKey = []byte("my_secret_key")
var blackList = make(map[string]bool)

var users = map[string]string{
	"user1": "password1",
	"user2": "password2",
}

// Create a struct to read the username and password from the request body
type Credentials struct {
	Password string `json:"password"`
	Username string `json:"username"`
}

type Claims struct {
	UserId int `json:"userId"`
	JWTId string `json:"jwtId"`
	jwt.StandardClaims
}

func LoginHandler(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
		case http.MethodPost:
			tokenString, err := GenerateToken(r)
			if err != nil {
				w.WriteHeader(http.StatusBadRequest)
				return
			}

    		fmt.Fprintf(w, tokenString)
		default:
			http.Error(w, "Invalid request method.", 405)
	}
}


func LogoutHandle(w http.ResponseWriter, r *http.Request) {
    switch r.Method {
		case http.MethodGet:
			_, jwtId, err := Authorized(r)
			if err != nil  {
				http.Error(w, "UnAuthorized.", 401)
				return
			}
			
			blackList[jwtId] = true
    		fmt.Fprintf(w, "")
		default:
			http.Error(w, "Invalid request method.", 405)
	}
}

func GenerateToken(r *http.Request) (string, error) {
	var creds Credentials
	err := json.NewDecoder(r.Body).Decode(&creds)
	
	if err != nil {
		return "", err
	}

	log.Println(creds)
	id, errLogin := db.LoginUser(creds.Username, creds.Password)
	if errLogin != nil {
		return "", errLogin
	}

	b := make([]byte, 16)
	rand.Read(b)
	expirationTime := time.Now().Add(5 * time.Minute)
	claims := &Claims{
		UserId: id,
		JWTId: fmt.Sprintf("%x-%x-%x-%x-%x", b[0:4], b[4:6], b[6:8], b[8:10], b[10:]),
		StandardClaims: jwt.StandardClaims{
			ExpiresAt: expirationTime.Unix(),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString(jwtKey)
}

func Authorized(r *http.Request) (int, string, error) {
	tokenString := r.Header.Get("Authorized")
	claims := jwt.MapClaims{}

	_, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return jwtKey, nil
	})

	jwtId := claims["jwtId"].(string)

	if blackList[jwtId] {
		return -1, jwtId, fmt.Errorf("Invalidated token")
	}

	if(err != nil) {
		return -1, jwtId, err
	} else {
		userId := int(claims["userId"].(float64))
		log.Println(userId)
		return userId, jwtId, nil
	}
}