// Code generated by go-swagger; DO NOT EDIT.

package goods

// This file was generated by the swagger tool.
// Editing this file might prove futile when you re-run the generate command

import (
	"context"
	"net/http"

	"github.com/go-openapi/runtime/middleware"
	"github.com/go-openapi/strfmt"
	"github.com/go-openapi/swag"
)

// SetGoodsHandlerFunc turns a function with the right signature into a set goods handler
type SetGoodsHandlerFunc func(SetGoodsParams) middleware.Responder

// Handle executing the request and returning a response
func (fn SetGoodsHandlerFunc) Handle(params SetGoodsParams) middleware.Responder {
	return fn(params)
}

// SetGoodsHandler interface for that can handle valid set goods params
type SetGoodsHandler interface {
	Handle(SetGoodsParams) middleware.Responder
}

// NewSetGoods creates a new http.Handler for the set goods operation
func NewSetGoods(ctx *middleware.Context, handler SetGoodsHandler) *SetGoods {
	return &SetGoods{Context: ctx, Handler: handler}
}

/* SetGoods swagger:route POST /setGoods Goods setGoods

SetGoods set goods API

*/
type SetGoods struct {
	Context *middleware.Context
	Handler SetGoodsHandler
}

func (o *SetGoods) ServeHTTP(rw http.ResponseWriter, r *http.Request) {
	route, rCtx, _ := o.Context.RouteInfo(r)
	if rCtx != nil {
		*r = *rCtx
	}
	var Params = NewSetGoodsParams()
	if err := o.Context.BindValidRequest(r, route, &Params); err != nil { // bind params
		o.Context.Respond(rw, r, route.Produces, route, err)
		return
	}

	res := o.Handler.Handle(Params) // actually handle the request
	o.Context.Respond(rw, r, route.Produces, route, res)

}

// SetGoodsBody set goods body
//
// swagger:model SetGoodsBody
type SetGoodsBody struct {

	// category
	Category string `json:"category,omitempty"`

	// name
	Name string `json:"name,omitempty"`

	// price
	Price int64 `json:"price,omitempty"`

	// wallet Id
	WalletID string `json:"walletId,omitempty"`
}

// Validate validates this set goods body
func (o *SetGoodsBody) Validate(formats strfmt.Registry) error {
	return nil
}

// ContextValidate validates this set goods body based on context it is used
func (o *SetGoodsBody) ContextValidate(ctx context.Context, formats strfmt.Registry) error {
	return nil
}

// MarshalBinary interface implementation
func (o *SetGoodsBody) MarshalBinary() ([]byte, error) {
	if o == nil {
		return nil, nil
	}
	return swag.WriteJSON(o)
}

// UnmarshalBinary interface implementation
func (o *SetGoodsBody) UnmarshalBinary(b []byte) error {
	var res SetGoodsBody
	if err := swag.ReadJSON(b, &res); err != nil {
		return err
	}
	*o = res
	return nil
}

// SetGoodsOKBody set goods o k body
//
// swagger:model SetGoodsOKBody
type SetGoodsOKBody struct {

	// code
	Code int64 `json:"code,omitempty"`

	// message
	Message string `json:"message,omitempty"`
}

// Validate validates this set goods o k body
func (o *SetGoodsOKBody) Validate(formats strfmt.Registry) error {
	return nil
}

// ContextValidate validates this set goods o k body based on context it is used
func (o *SetGoodsOKBody) ContextValidate(ctx context.Context, formats strfmt.Registry) error {
	return nil
}

// MarshalBinary interface implementation
func (o *SetGoodsOKBody) MarshalBinary() ([]byte, error) {
	if o == nil {
		return nil, nil
	}
	return swag.WriteJSON(o)
}

// UnmarshalBinary interface implementation
func (o *SetGoodsOKBody) UnmarshalBinary(b []byte) error {
	var res SetGoodsOKBody
	if err := swag.ReadJSON(b, &res); err != nil {
		return err
	}
	*o = res
	return nil
}