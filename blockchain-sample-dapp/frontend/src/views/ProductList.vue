<template>
    <div style="height:700px;">
        <div class="sticky-md-top text-center">
          <h1>Product List</h1>
          <hr class="border border-primary border-2 opacity-30">
        </div>
        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <button class="btn btn-primary me-md-2" type="button" @click="goCreateProduct">Enroll</button>                        
        </div>
        <br/>
        <table class="table">
        <thead class="table-light">
            <tr>            
                <th scope="col">NO</th>
                <th scope="col">ID</th>
                <th scope="col">Product Name</th>
                <th scope="col">Category</th>                
            </tr>
        </thead>
        <tbody>
            <tr v-for="(data, index) in productData" :key="index" @click="goViewProduct(data.id)" style="cursor:pointer">                
                <td>{{index +1}}</td>
                <td>{{data.id}}</td>
                <td>{{data.name}}</td>
                <td>{{data.category}}</td>                
            </tr>           
        </tbody>
        </table>        
    </div>
</template>
<script>
export default {
    name: '',
    components: {},
    data() {
        return {
            productData:[] 
        };
    },
    setup() {},
    created() {
        this.init();
    },
    mounted() {
        
    },
    unmounted() {},
    methods: {
        async init() {            
            const result = await this.$http.get('/api/product', {});            
            this.productData = JSON.parse(JSON.stringify(result.data));
            console.log(this.productData);            
        },
        goCreateProduct() {
            this.$router.push("/CreateProduct")

        },
        goViewProduct(productId) {
            this.$router.push({path: '/DetailProduct', query: {productId:productId}})
        }


    }
}
</script>