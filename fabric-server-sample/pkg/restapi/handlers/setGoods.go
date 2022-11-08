package handlers

import (
	"fmt"
	"github.com/NaverCloudPlatform/fabric-server-sample/internal/sdk"
	"github.com/NaverCloudPlatform/fabric-server-sample/pkg/restapi/blockchain_sample_server_package_controller/goods"
	"github.com/go-openapi/runtime/middleware"
	"net/http"
)

func SetGoods(params goods.SetGoodsParams) middleware.Responder {
	name := params.Body.Name
	category := params.Body.Category
	price := params.Body.Price
	walletId := params.Body.WalletID

	conn, err := sdk.Client("orgmsp", "mychannel")
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}
	defer conn.Close()

	_, err = conn.Invoke("firstcc", "setGoods", name, category, fmt.Sprint(price), walletId)
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}

	return goods.NewSetGoodsOK().WithPayload(&goods.SetGoodsOKBody{
		Code:    0,
		Message: "success",
	})
}
