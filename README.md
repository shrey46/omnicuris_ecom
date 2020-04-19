# omnicuris_ecom
Basic ecom project with REST api's

Java version : 1.8

Framework : Spring-Boot

Database : MongoDb

Mongo Port : 27071

TomcatServer Port : 4646

Github link for cloning:https://github.com/shrey46/omnicuris_ecom.git

Sample data of stock and user is added with file for usage : STOCK_DATA.json , USER_DATA .json

Mongodb database = ecom-dev

Mongodb Collections = omnicomUsers , omnicomOrders ,omnicomStocks

API's details : 

ROOT : /omnicom

POST api's : 

/addUsers : adding single/multiple users
/addProduct : Adding single product to stock
/addProducts : Saving multiple products to stock
/addOrder : Saving single order
/addOrders : Saving bulk orders

GET api's :

/findAllProducts : Fetching all product in the cart
/findProduct/{search} : Searching product's with id(give complete), productBrand,productCategory, productSubCategory using regex
/findOrders/{search} : SEARCH ORDERS OF A USER WITH EMAIL OR USER-ID
/findAllorders : search all orders made by all users

DELETE api's :
/deleteProduct/{id} : Deleting product in the stock by its id

PUT api's :

/updateProduct/{id} : Updating product in the stock by its id




NOTE : 
1. Please use the above configuration for running the project
2. Order placed will be delivered after 7 days of placing it (assumption)
3. Time is saved in millisecond time format i.e. System.currentTimeMillis() in mongoDb
4. In any of the search where id is used for searching give complete id from mongoDb
5. Order can be placed with userEmail and product Id
6. Validations are added for checking unique emails accordingly

