//Dashbaord APIs
var express = require('express');
var router = express.Router();
var contract = require('../wallet/wallet.js');

router.get('/facility/count', async function (req, res, next) {
    try {
        const facilityContract = await contract.getFacilityContract();
        const facilityCountResult = await facilityContract.evaluateTransaction('getFacilityCount');
        console.log("result : " + facilityCountResult);
        res.send(facilityCountResult);
    } catch (e) {
        console.log("error : " + e);
        res.send(e);
    }
});

router.get('/product/count', async function(req, res, next) {
    try {
        const productContract = await contract.getProductContract();    
        const productCountResult = await productContract.evaluateTransaction('getProductCount');
        console.log("result : " + productCountResult);
        res.send(productCountResult);
    } catch(e) {
        console.log("error : " + e);
        res.send(e);
    }
})

router.get('/addBlockEvent', async function (req, res, next) {
    const network = await contract.getNetwork();

    const listener = await network.addBlockListener('my-block-listener', (error, block) => {
        if (err) {
            console.error(err);
            return;
        }
        console.log(`Block: ${block}`);
    });
    console.log("added block event ");
    //res.status(200).send("OK");
});

module.exports = router;