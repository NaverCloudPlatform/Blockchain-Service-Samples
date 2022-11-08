//product API
var express = require('express');
var router = express.Router();
var contract = require('../wallet/wallet.js');

router.post('/', async function (req, res, next) {
    console.log("create product : " + JSON.stringify(req.body.params[0]));
    try {
        const params = JSON.parse(JSON.stringify(req.body.params[0]));
        const id = "product:" + params.name;        
        const productContract = await contract.getProductContract();    
        const registerProductResult = await productContract.submitTransaction('registerProduct', id, params.name, params.type, params.facilityId);    
        console.log("result : " + registerProductResult);
        res.send(registerProductResult);                    
    } catch (e) {
        console.log("error : " + e.message);          
        res.status(500).send({
            error: e.message
        });        
    }
});

router.get('/', async function (req, res, next) {
    try {
        const productContract = await contract.getProductContract();    
        const productList = await productContract.evaluateTransaction('getAllProduct');
        console.log("result : " + productList);        
        res.send(productList);            
    } catch (e) {
        console.log("error : " + e.message);          
        res.status(500).send({
            error: e.message
        });   
    }
});

router.get("/:id", async function (req, res, next) {
    try {
        console.log("get product param : "+req.params);
        const id = req.params.id;
        
        const productContract = await contract.getProductContract();
        const productResult = await productContract.evaluateTransaction('getProduct', id);
        console.log("result : " + productResult);
        console.log('Transaction has been submitted');
        res.send(productResult);
    } catch (e) {
        console.log("error : " + e);
        res.send(e);
    }
}); 

router.get("/:id/actionHistory", async function (req, res, next) {
    try {       
         const productName = req.params.id;       
         const productContract = await contract.getActionContract();
         const productActionResult = await productContract.evaluateTransaction('getActionHistory', createActionId(productName));
         console.log("result: " + productActionResult);
         res.send(productActionResult);
    } catch (e) {
        console.log("error : " + e);
        res.status(400).send(e);
    }
});

router.post("/action", async function (req, res, next) {
    console.log("insert product action : " + JSON.stringify(req.body.params[0]));
    try {
        const params = JSON.parse(JSON.stringify(req.body.params[0]));        
        const actionContract = await contract.getActionContract();    
        const registerProductActionResult = await actionContract.submitTransaction('registerAction', createActionId(params.name), params.productId, params.facilityId, params.action);    
        console.log("result : " + registerProductActionResult);
        res.send(registerProductActionResult);                    
    } catch (e) {
        console.log("error : " + e.message);          
        res.status(500).send({
            error: e.message
        });        
    }
});

function createActionId(productId) {
    return "history:product:" + productId; 
}

module.exports = router;