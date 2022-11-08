import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Dashboard from '@/views/Dashboard.vue'
import FacilicyList from '@/views/FacilityList.vue'
import ProductList from '@/views/ProductList.vue'
import CreateFacility from '@/views/CreateFacility.vue'
import DetailFacility from '@/views/DetailFacility.vue'
import CreateProduct from '@/views/CreateProduct.vue'
import DetailProduct from '@/views/DetailProduct.vue'


export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/Dashboard',
      name: 'Dashboard',
      component: Dashboard
    },
    {
      path: '/FacilityList',
      name: 'FacilicyList',
      component: FacilicyList
    },
    {
      path: '/ProductList',
      name: 'ProductList',
      component: ProductList
    },
    {
      path: '/CreateFacility',
      name: 'CreateFacility',
      component: CreateFacility
    },
    {
      path: '/CreateProduct',
      name: 'CreateProduct',
      component: CreateProduct
    },
    {
      path: '/DetailProduct',
      name: 'DetailProduct',
      component: DetailProduct
    },
    {
      path: '/DetailFacility',
      name: 'DetailFacility',
      component: DetailFacility
    }
  ]
})
