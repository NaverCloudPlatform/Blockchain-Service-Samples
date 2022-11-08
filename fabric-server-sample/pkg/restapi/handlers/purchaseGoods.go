package handlers

import (
	"github.com/NaverCloudPlatform/fabric-server-sample/internal/sdk"
	"github.com/NaverCloudPlatform/fabric-server-sample/pkg/restapi/blockchain_sample_server_package_controller/goods"
	"github.com/go-openapi/runtime/middleware"
	"net/http"
)

func PurchaseGoods(params goods.PostPurchaseGoodsParams) middleware.Responder {
	customerId := params.Body.WalletID
	goodsKey := params.Body.Key

	conn, err := sdk.Client("orgmsp", "mychannel")
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}
	defer conn.Close()

	_, err = conn.Invoke("firstcc", "purchaseGoods", customerId, goodsKey)
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}

	return goods.NewPostPurchaseGoodsOK().WithPayload(&goods.PostPurchaseGoodsOKBody{
		Code:    0,
		Message: "success",
	})
}
