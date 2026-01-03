# API-Documentation

## Headers:
All requests (except auth) require:
* Authorization: Bearer <access_token>
* x-tenant-id: <tenantId>


## Authentication:

Body
* email, password

Request
* POST /auth/login

Response
* accessToken

Refresh the token
* POST auth/refresh

Logout
* POST auth/logout


---


## Products

Request
* POST /product
* Create a product
* GET /product
* supports: page, size, sku filter


---


## Order

Request
* POST /order/checkout
* Creates order from active cart
