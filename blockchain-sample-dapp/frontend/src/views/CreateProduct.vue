<template>
    <div>
        <div class="sticky-md-top text-center">
            <h1>Create Product</h1>
            <hr class="border border-primary border-2 opacity-30">
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="productName" class="col-sm-2 col-form-label">Product Name</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="productName" v-model="product.name">
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="facilityId" class="col-sm-2 col-form-label">Facility ID</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="facilityId" v-model="product.facilityId">
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="type" class="col-sm-2 col-form-label">Category</label>
            <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" id="category" v-model="product.category">
                    <option value="meet">육류</option>
                    <option value="cereal">곡류</option>
                    <option value="proceedfood">가공식품</option>
                    <option value="milk">유제품</option>
                    <option value="fruits">과일</option>
                    <option value="vegetable">채소</option>
                </select>
            </div>
        </div>        
        <div class="d-grid gap-4 d-md-flex justify-content-md-end col-7" >
            <button class="btn btn-primary me-md-2" type="button" @click="productInsert()" v-show="showButtons">Create</button>            
            <button class="btn btn-primary" type="button" @click="goBack()" v-show="showButtons">Cancel</button>
        </div>
        <div class="d-flex justify-content-center"  >        
            <div class="spinner-grow text-success" role="status" v-show="showSpinner">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name: '',
    components: {},
    data() {
        return {
            product: {
                name: "",
                facilityId: "",
                type: ""
            },
            showButtons: true,
            showSpinner: false
        };
    },
    setup() {},
    created() {},
    mounted() {},
    unmounted() {},
    methods: {
        goBack() {
            this.$router.go(-1);
        },
        productInsert() {
            if (this.product.name == "") {
              return  this.$swal('Missing', 'Please insert product name', 'OK');
            }
            if (this.product.facilityId == "") {
              return  this.$swal('Missing', 'Please insert product facility ID', 'OK');
            }
            if (this.product.category == "") {
              return  this.$swal('Missing', 'Please insert product category', 'OK');
            }
            this.$swal.fire({
                title: 'Would you like to create product?',
                showCancelButton: true,
                confirmButtonText: `Create`,
                cancelButtonText: `Cancel`
            }).then(async (result) => {
                if (result.isConfirmed) {
                    this.startProcess();
                    try {
                        let apiResult = await this.$http.post('/api/product', { params: [this.product] });
                        console.log(apiResult);                    
                        if (apiResult.status == "200") {                        
                            this.$swal.fire('Success to create', '', 'success');
                            this.endProcess(); 
                            this.$router.push({ path: '/ProductList' });                              
                        } else {
                            this.$swal.fire('Failed to create!', apiResult, 'fail');
                            this.endProcess();   
                        }              
                    } catch (e) {
                        this.$swal.fire('Failed to create!', e, 'fail');
                        this.endProcess();   
                    }
                            
                } 
            });
        },
        startProcess() {
            this.showButtons = false;
            this.showSpinner = true;
        },
        endProcess() {
            this.showButtons = true;
            this.showSpinner = false;
        }
    }
}
</script>