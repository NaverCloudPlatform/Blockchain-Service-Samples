//facility APIs
var express = require('express');
var router = express.Router();
var contract = require('../wallet/wallet.js');

router.post('/', async function (req, res, next) {

    console.log("create facility : " + JSON.stringify(req.body.params[0]));
    try {
        const params = JSON.parse(JSON.stringify(req.body.params[0]));
        const id = "facility:" + params.name;        
        const facilityContract = await contract.getFacilityContract();    
        const registerFacilityResult = await facilityContract.submitTransaction('registerFacility', id,  params.name, params.location, params.category, params.telNo, params.emailAddress);   
        res.send(registerFacilityResult);  
                  
    } catch (e) {
        console.log("error : " + e);
        res.send(e);
    }
    
});

router.get('/', async function (req, res, next) {
    try {
        const facilityContract = await contract.getFacilityContract();    
        const facilityResult = await facilityContract.evaluateTransaction('getAllFacility');
        console.log("result : " + facilityResult);
        res.send(facilityResult);
    } catch (e) {
        console.log("error : " + e);
        res.send(e);
    }
    
});

router.get('/:id', async function (req, res, next) {
    try {
        const { id } = req.params;
        const facilityContract = await contract.getFacilityContract();    
        const facilityResult = await facilityContract.evaluateTransaction('getFacility', id);
        console.log("result : " + facilityResult);
        res.send(facilityResult);
    } catch (e) {
        console.log("error : " + e);
        res.send(e);
    }
    
});

router.put('/', async function (req, res, next) {

    console.log("update facility : " + JSON.stringify(req.body.params[0]));
    try {
        const params = JSON.parse(JSON.stringify(req.body.params[0]));            
        const facilityContract = await contract.getFacilityContract();    
        const updateFacilityResult = await facilityContract.submitTransaction('updateFacility', params.id, params.name, params.location, params.category, params.telNo, params.emailAddress);    
        console.log("result : " + updateFacilityResult);
        res.send(updateFacilityResult);                    
    } catch (e) {
        console.log("error : " + e);
        res.send(e);
    }
    
});

router.delete('/:id', async function (req, res, next) {

    console.log("delete facility : " + req.params);
    try {              
        const { id } = req.params;        
        const facilityContract = await contract.getFacilityContract();    
        const deleteFacilityResult = await facilityContract.submitTransaction('deleteFacility', id);    
        console.log("result : " + deleteFacilityResult);
        res.send(deleteFacilityResult);                    
    } catch (e) {
        console.log("error : " + e);
        res.send(e);
    }
    
});

module.exports = router;