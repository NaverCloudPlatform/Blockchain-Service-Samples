package sdk

import (
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric-sdk-go/pkg/core/config"
	"github.com/hyperledger/fabric-sdk-go/pkg/gateway"
	flag "github.com/spf13/pflag"
	"io/ioutil"
	"path/filepath"
)

var (
	connectionProfilePath string
	clientCertificatePath string
)

func InitFlags() {
	flag.StringVarP(&connectionProfilePath, "connection-profile-path", "p", "", "connection profile file to connect fabric network")
	flag.StringVarP(&clientCertificatePath, "client-certificate-path", "c", "", "Certificate file for identification")
}

type Certificate struct {
	TlsKey  string `json:"tls_key"`
	TlsCert string `json:"tls_cert"`
	Name    string `json:"name"`
	Cert    string `json:"cert"`
	Type    string `json:"type"`
	Key     string `json:"key"`
}

func (c *Certificate) GetCert() ([]byte, error) {
	return base64.StdEncoding.DecodeString(c.Cert)
}

func (c *Certificate) GetKey() ([]byte, error) {
	return base64.StdEncoding.DecodeString(c.Key)
}

type connection struct {
	gateway *gateway.Gateway
	network *gateway.Network
}

func Client(msp, channel string) (*connection, error) {
	wallet, err := gateway.NewFileSystemWallet("wallet")
	if err != nil {
		fmt.Printf("Failed to create wallet: %s\n", err)
		return nil, err
	}

	if !wallet.Exists("client") {
		err = populateWallet("client", msp, wallet)
		if err != nil {
			fmt.Printf("Failed to populate wallet contents: %s\n", err)
			return nil, err
		}
	}

	gw, err := gateway.Connect(
		gateway.WithConfig(config.FromFile(filepath.Clean(connectionProfilePath))),
		gateway.WithIdentity(wallet, "client"),
	)
	if err != nil {
		fmt.Printf("Failed to connect to gateway: %s\n", err)
		return nil, err
	}

	network, err := gw.GetNetwork(channel)
	if err != nil {
		return nil, err
	}

	return &connection{
		gateway: gw,
		network: network,
	}, nil
}

func populateWallet(label string, msp string, wallet *gateway.Wallet) error {
	pBytes, err := ioutil.ReadFile(filepath.Clean(clientCertificatePath))
	if err != nil {
		return err
	}

	var certificate Certificate

	if err := json.Unmarshal(pBytes, &certificate); err != nil {
		return err
	}

	cert, err := certificate.GetCert()
	if err != nil {
		return err
	}

	key, err := certificate.GetKey()
	if err != nil {
		return err
	}

	identity := gateway.NewX509Identity(msp, string(cert), string(key))

	if err = wallet.Put(label, identity); err != nil {
		return err
	}
	return nil
}

func (c *connection) Invoke(contractId string, fn string, args ...string) ([]byte, error) {
	contract := c.network.GetContract(contractId)

	return contract.SubmitTransaction(fn, args...)
}

func (c *connection) Query(contractId string, fn string, args ...string) ([]byte, error) {
	contract := c.network.GetContract(contractId)

	return contract.EvaluateTransaction(fn, args...)
}

func (c *connection) Close() {
	c.gateway.Close()
}
