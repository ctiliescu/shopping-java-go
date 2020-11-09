FROM golang
ADD . .

RUN go get -v -u github.com/dgrijalva/jwt-go
RUN go get -v -u github.com/hashicorp/go-memdb

EXPOSE 7010

CMD ["go", "run", "main.go"]
