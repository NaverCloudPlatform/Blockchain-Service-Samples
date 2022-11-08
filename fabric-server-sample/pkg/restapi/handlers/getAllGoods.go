package handlers

import (
	"encoding/json"
	"github.com/NaverCloudPlatform/fabric-server-sample/internal/sdk"
	"github.com/NaverCloudPlatform/fabric-server-sample/pkg/restapi/blockchain_sample_server_package_controller/goods"
	"github.com/go-openapi/runtime/middleware"
	"net/http"
)

type getAllGoodsQueryResult struct {
	Key    string `json:"key"`
	Record struct {
		Name     string `json:"name"`
		Category string `json:"category"`
		Price    int64  `json:"price"`
		WalletId string `json:"walletId"`
	} `json:"record"`
}

func GetAllGoods(params goods.GetGetAllGoodsParams) middleware.Responder {
	conn, err := sdk.Client("orgmsp", "mychannel")
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}
	defer conn.Close()

	out, err := conn.Query("firstcc", "getAllGoods")
	if err != nil {
		return middleware.Error(http.StatusInternalServerError, err.Error())
	}

	queryResults := []*getAllGoodsQueryResult{}
	_ = json.Unmarshal(out, &queryResults)

	results := []*goods.GetGetAllGoodsOKBodyItems0{}

	for _, g := range queryResults {
		results = append(results, &goods.GetGetAllGoodsOKBodyItems0{
			Category: g.Record.Category,
			Key:      g.Key,
			Name:     g.Record.Name,
			Price:    g.Record.Price,
			WalletID: g.Record.WalletId,
		})
	}

	return goods.NewGetGetAllGoodsOK().WithPayload(results)
}
