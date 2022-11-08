package handlers

import (
	"encoding/json"
	"github.com/NaverCloudPlatform/fabric-server-sample/internal/sdk"
	"github.com/NaverCloudPlatform/fabric-server-sample/pkg/restapi/blockchain_sample_server_package_controller/wallet"
	"github.com/go-openapi/runtime/middleware"
	"net/http"
)

type Wallet struct {
	Name  string `json:"name"`
	Id    string `json:"id"`
	Token int64  `json:"token"`
}

func GetWallet(params wallet.GetWalletParams) middleware.Responder {
	walletId := params.WalletID

	conn, err := sdk.Client("orgmsp", "mychannel")
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}
	defer conn.Close()

	out, err := conn.Query("firstcc", "getWallet", walletId)
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}

	w := Wallet{}
	_ = json.Unmarshal(out, &w)

	return wallet.NewGetWalletOK().WithPayload(&wallet.GetWalletOKBody{
		ID:    w.Id,
		Name:  w.Name,
		Token: w.Token,
	})
}
