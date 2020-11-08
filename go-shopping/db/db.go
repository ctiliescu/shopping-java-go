package db

import (
    "fmt"
	"github.com/hashicorp/go-memdb"
    "log"
)

var dataBase *memdb.MemDB

// Create a sample struct
type Person struct {
	Id int
	Name string
	Email  string
	Password   string
}

type Product struct {
	Id int
	Name string
	Price  int
	Stock   int
}

func Start() {
	// Create the DB schema
	schema := &memdb.DBSchema{
		Tables: map[string]*memdb.TableSchema{
			"user": &memdb.TableSchema{
				Name: "user",
				Indexes: map[string]*memdb.IndexSchema{
					"id": &memdb.IndexSchema{
						Name:    "id",
						Unique:  true,
						Indexer: &memdb.IntFieldIndex{Field: "Id"},
					},
					"email": &memdb.IndexSchema{
						Name:    "email",
						Unique:  false,
						Indexer: &memdb.StringFieldIndex{Field: "Email"},
					},
				},
			},
			"product": &memdb.TableSchema{
				Name: "product",
				Indexes: map[string]*memdb.IndexSchema{
					"id": &memdb.IndexSchema{
						Name:    "id",
						Unique:  true,
						Indexer: &memdb.IntFieldIndex{Field: "Id"},
					},
				},
			},
		},
	}

	// Create a new data base
	db, err := memdb.NewMemDB(schema)
	if err != nil {
		panic(err)
	}

	dataBase = db

	// Create a write transaction
	txn := db.Txn(true)

	// Insert some people
	people := []*Person{
		&Person{1, "Jon Doe", "jon.doe@company.com", "1234"},
		&Person{2, "Jon Doe2", "jon.doe2@company.com", "1234"},
	}

	for _, p := range people {
		if err := txn.Insert("user", p); err != nil {
			panic(err)
		}
	}

	product := []*Product{
		&Product{1, "Product 1", 100, 2},
		&Product{2, "Product 2", 200, 3},
	}

	for _, p := range product {
		if err := txn.Insert("product", p); err != nil {
			panic(err)
		}
	}

	// Commit the transaction
	txn.Commit()
}

func LoginUser(email string, password string) (int, error) {
	// Create read-only transaction
	var txn = dataBase.Txn(false)
	defer txn.Abort()

	raw, err := txn.First("user", "email", email)

	if err != nil { 
		return -1, err 
	} else if raw == nil || raw.(*Person).Password != password {
		return -1, fmt.Errorf("Invalidated password")
	}

	return raw.(*Person).Id, nil 
}

func Order(order map[int]int) (bool) {
	// Create read-only transaction
	txn := dataBase.Txn(true)

	for k, v := range order {
		raw, _ := txn.First("product", "id", k)
			log.Println(raw)
		if (raw != nil &&  raw.(*Product).Stock >= v) {
			product := raw.(*Product) 
			product.Stock = product.Stock - v
			log.Println(product)
			if err := txn.Insert("product", product); err != nil {

			log.Println(err)
				txn.Abort()
				return false;	
			}
		} else {
			txn.Abort()
			return false;
		}
	}

	txn.Commit()

	return true
}

func RevertOrder(order map[int]int) (bool) {
	txn := dataBase.Txn(true)

	for k, v := range order {
		raw, _ := txn.First("product", "id", k)
		if (raw != nil) {
			product := raw.(*Product) 
			product.Stock = product.Stock + v
			txn.Insert("Product", product)
		} else {
			txn.Abort()
			return false;
		}
	}

	txn.Commit()

	return true
}