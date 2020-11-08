package main

import (
	auth "./auth"
	cart "./cart"
	db "./db"
    "net/http"
)

func main() {
    db.Start()
	db.LoginUser("jon.doe@company.com", "1234")
    http.HandleFunc("/api/v1/users/login", auth.LoginHandler)
    http.HandleFunc("/api/v1/users/logout", auth.LogoutHandle)
    http.HandleFunc("/api/v1/carts/add", cart.AddHandle)
    http.HandleFunc("/api/v1/carts/order", cart.OrderHandle)
    http.ListenAndServe(":7010", nil)

}

