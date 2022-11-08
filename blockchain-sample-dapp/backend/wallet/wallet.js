const { Wallets, Gateway } = require('fabric-network');
const fs = require('fs');
const path = require('path');

//FIXME connection profile path
const connectionProfilePath = "connection_property.json";
//FIXME user certification path
const userInfoPath = "client.json";
//FIXME chaincode name
const chnnelName = "tracking-sample";


async function getNetwork() {   

    // Parse the connection profile. This would be the path to the file downloaded from NaverCloudPlatform
    const ccpPath = path.resolve(__dirname, connectionProfilePath);
    const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));
    const mspId = ccp.client.organization;
    
    // load the exported user certification
    const userPath = path.resolve(__dirname, userInfoPath);
    const user = JSON.parse(fs.readFileSync(userPath, 'utf8'));

    // Create a new file system based wallet for managing identities.
    const walletPath = path.join(process.cwd(), 'wallet');
    const wallet = await Wallets.newFileSystemWallet(walletPath);    
    var identity = await wallet.get(user.name);    
    if (!identity) {
        const x509Identity = {
            credentials: {
                certificate: Buffer.from(user.cert, 'base64').toString('utf8'),
                privateKey: Buffer.from(user.key, 'base64').toString('utf8'),
            },
            mspId: mspId,
            type: 'X.509',
        };
        await wallet.put(user.name, x509Identity);
        
        identity = await wallet.get(user.name);
    }    
    // Create a new gateway, and connect to the gateway peer node(s). The identity
    // specified must already exist in the specified wallet.
    const gateway = new Gateway();
    await gateway.connect(ccp, { wallet: wallet, identity: user.name, discovery: { enabled: true, asLocalhost: false } });
    // Get the network (channel) our contract is deployed to.
    const network = await gateway.getNetwork(chnnelName);
    return network;
}


module.exports.getFacilityContract = async function () {
    try {
        const network = await getNetwork();        
        // Get the each smart contract from the network channel.
        const facilityContract = network.getContract('tracking', 'FacilityContract');
        //const result = await facilityContract.evaluateTransaction('getAllFacility');        
        return facilityContract;    
    } catch (exception) {
        console.log(exception)
    }
    
};

module.exports.getProductContract = async function () {
    try {
        const network = await getNetwork();        
        // Get the each smart contract from the network channel.
        const facilityContract = network.getContract('tracking', 'ProductContract');     
        return facilityContract;    
    } catch (exception) {
        console.log(exception)
    }
};
module.exports.getActionContract = async function () {
    try {
        const network = await getNetwork();        
        // Get the each smart contract from the network channel.
        const facilityContract = network.getContract('tracking', 'ActionContract');     
        return facilityContract;    
    } catch (exception) {
        console.log(exception)
    }
};
module.exports.getNetwork = async function () {
    try {
        const network = await getNetwork();              
        return network;    
    } catch (exception) {
        console.log(exception)
    }
};