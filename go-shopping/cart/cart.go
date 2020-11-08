package cart

import (
    "fmt"
    "time"
    "net/http"
    "encoding/json"
    "math/rand"
	auth "../auth"
	db "../db"
)

var m = make(map[int]map[int]int)

type Product struct {
	ProductId int `json:"product_id"`
	Quantity int `json:"quantity"`
}

func AddHandle(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
		case http.MethodPost:
			id, _, err := auth.Authorized(r)
			if err != nil {
				http.Error(w, "UnAuthorized.", 401)
				return
			}

			var product Product
			err = json.NewDecoder(r.Body).Decode(&product)
	
			if err != nil {
				w.WriteHeader(http.StatusBadRequest)
				return
			}

			if m[id] == nil {
				userMap := make(map[int]int)
				userMap[product.ProductId] = product.Quantity
				m[id] = userMap
			} else {
				m[id][product.ProductId] = m[id][product.ProductId] + product.Quantity
			}
    		fmt.Fprintf(w, "add to cart")
		default:
			http.Error(w, "Invalid request method.", 405)
	}
}


func OrderHandle(w http.ResponseWriter, r *http.Request) {
    switch r.Method {
		case http.MethodGet:
			id, _, err := auth.Authorized(r)
			if err != nil {
				http.Error(w, "UnAuthorized.", 401)
				return
			}

			order := m[id]

			if db.Order(order) {
				if CheckPayment() {
	    			delete(m, id)
		    		fmt.Fprintf(w, "checkout")
		    	} else {
		    		http.Error(w, "Payment failed.", 400)
		    		db.RevertOrder(order)
		    	}
	    	} else {
	    		delete(m, id)
	    		http.Error(w, "Invalid stock.", 400)
	    	}
		default:
			http.Error(w, "Invalid request method.", 405)
	}
}

func CheckPayment()(bool) {
	time.Sleep(2 * time.Second)
	return rand.Float32() < 0.5
}