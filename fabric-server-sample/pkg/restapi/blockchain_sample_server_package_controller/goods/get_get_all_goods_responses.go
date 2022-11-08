// Code generated by go-swagger; DO NOT EDIT.

package goods

// This file was generated by the swagger tool.
// Editing this file might prove futile when you re-run the swagger generate command

import (
	"net/http"

	"github.com/go-openapi/runtime"
)

// GetGetAllGoodsOKCode is the HTTP code returned for type GetGetAllGoodsOK
const GetGetAllGoodsOKCode int = 200

/*GetGetAllGoodsOK ok

swagger:response getGetAllGoodsOK
*/
type GetGetAllGoodsOK struct {

	/*
	  In: Body
	*/
	Payload []*GetGetAllGoodsOKBodyItems0 `json:"body,omitempty"`
}

// NewGetGetAllGoodsOK creates GetGetAllGoodsOK with default headers values
func NewGetGetAllGoodsOK() *GetGetAllGoodsOK {

	return &GetGetAllGoodsOK{}
}

// WithPayload adds the payload to the get get all goods o k response
func (o *GetGetAllGoodsOK) WithPayload(payload []*GetGetAllGoodsOKBodyItems0) *GetGetAllGoodsOK {
	o.Payload = payload
	return o
}

// SetPayload sets the payload to the get get all goods o k response
func (o *GetGetAllGoodsOK) SetPayload(payload []*GetGetAllGoodsOKBodyItems0) {
	o.Payload = payload
}

// WriteResponse to the client
func (o *GetGetAllGoodsOK) WriteResponse(rw http.ResponseWriter, producer runtime.Producer) {

	rw.WriteHeader(200)
	payload := o.Payload
	if payload == nil {
		// return empty array
		payload = make([]*GetGetAllGoodsOKBodyItems0, 0, 50)
	}

	if err := producer.Produce(rw, payload); err != nil {
		panic(err) // let the recovery middleware deal with this
	}
}