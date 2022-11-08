<template>
    <div>
        <div class="sticky-md-top text-center">
            <h1>View Product</h1>
            <hr class="border border-primary border-2 opacity-30">
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="productId" class="col-sm-2 col-form-label">Product ID</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="productId" v-model="product.id" disabled>
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="productName" class="col-sm-2 col-form-label">Product Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="productName" v-model="product.name" disabled>
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="facilityId" class="col-sm-2 col-form-label">facility ID</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="facilityId" v-model="product.facilityId" disabled>
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="type" class="col-sm-2 col-form-label">Category</label>
            <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" id="type" v-model="product.category" disabled>
                    <option value="meet">육류</option>
                    <option value="cereal">곡류</option>
                    <option value="proceedfood">가공식품</option>
                    <option value="milk">유제품</option>
                    <option value="fruits">과일</option>
                    <option value="vegetable">채소</option>
                </select>
            </div>
        </div>             
       <br/>
        <div class="sticky-md-top text-center">
            <h1>Product Action History  
                <button class="btn btn-secondary" type="button" @click="loadActionHistory()" v-show="showButtons">Reload History</button>
                <b-button class="btn btn-primary" v-b-modal.regist-new-action>Register New Action</b-button>
            </h1> 
            <hr class="border border-primary border-2 opacity-30">
            <div class="d-grid gap-4 d-md-flex justify-content-md-end col-7" >               
            </div>
        </div>
        <table class="table">
        <thead class="table-light">
            <tr>            
                <th scope="col">NO</th>
                <th scope="col">Facility ID</th>
                <th scope="col">Action</th>  
                <th scope="col">Time</th>              
            </tr>
        </thead>
        <tbody>
            <tr v-for="(data, index) in actionList" :key="index">                
                <td>{{index +1}}</td>                
                <td>{{data.facilityId}}</td>
                <td>{{data.action}}</td>                
                <td>{{data.processTime}}</td>
            </tr>
            <tr v-if="actionList.length == 0" >
                <td colspan="3" style="text-align:center;">no history</td>
            </tr>           
        </tbody>
        </table>    
       
        <div>           
            <b-modal
                id="regist-new-action"
                ref="modal"
                title="Register New Action"
                @show="resetModal"
                @hidden="resetModal"
                @ok="handleOk"
                centered
            >
            <form ref="form" @submit.stop.prevent="handleSubmit">
                <b-form-group label="Facility ID" label-for="popup-facilityId" invalid-feedback="FacilityID is required" :state="action.facilityIDState">
                    <!-- <b-form-input id="popup-facilityId" v-model="action.facilityId" :state="action.facilityIDState" required></b-form-input> -->

                    <div class="col-sm-10">
                        <select class="form-select" id="popup-facilityId" v-model="action.facilityId" required>                            
                            <option v-for="(data, index) in facilityData" :key="index" :value="data.id">{{data.name}}</option>                                                            
                        </select>
                    </div>
                </b-form-group>
                <b-form-group label="Action" label-for="popup-action" invalid-feedback="Action is required" :state="action.actionState">
                    <b-form-input id="popup-action" v-model="action.action" :state="action.actionState" required></b-form-input>
                </b-form-group>                
            </form>
            <div class="d-flex justify-content-center"  >        
                <div class="spinner-grow text-success" role="status" v-show="showSpinner">
                    <span class="visually-hidden">Loading...</span>
                </div>
            </div>
            </b-modal>
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
                id: "",
                name: "",        
                category: ""
            },
            action: {
                productId: "",
                facilityId: "",
                action: "",
                actionState: null,
                facilityIDState: null                
            },
            facilityData: [],
            actionList: [],            
            showButtons: true,
            showSpinner: false
        };
    },
    setup() {},
    created() {
        this.product.id = this.$route.query.productId;
        this.action.productId = this.$route.query.productId;
        this.init();
    },
    mounted() {},
    unmounted() {},
    methods: {
        async init() {            
            const result = await this.$http.get('/api/product/'+this.product.id);            
            this.product = JSON.parse(JSON.stringify(result.data));
            this.product.id = this.$route.query.productId;
            console.log(this.product);
            this.loadActionHistory(); 
            const facilityResult = await this.$http.get('/api/facility', {});                        
            this.facilityData = JSON.parse(JSON.stringify(facilityResult.data));
            console.log(this.facilityData);
        },
        async loadActionHistory() {
            console.log("load action history")
            try {
                const result = await this.$http.get('/api/product/'+this.product.name+'/actionHistory');            
                this.actionList = JSON.parse(JSON.stringify(result.data));            
                console.log(this.action);
            } catch (e) {                
               this.actionList = [];
            }             
        },              
        startProcess() {
            this.showButtons = false;
            this.showSpinner = true;
        },
        endProcess() {
            this.showButtons = true;
            this.showSpinner = false;
        },
        resetModal() {
            this.action.action = '';
            this.actionState = null;
            this.action.facilityId = '';
            this.action.facilityIDState = null;        
        },
        handleOk(bvModalEvent) {
            // Prevent modal from closing
            bvModalEvent.preventDefault()
            // Trigger submit handler
            this.handleSubmit()
        },
        async handleSubmit() {
            // Exit when the form isn't valid
            if (!this.checkFormValidity()) {
                return
            }
            // register action
            await this.registerAction();            
            // this.$nextTick(() => {              
               
            // })
        },
        checkFormValidity() {
            const valid = this.$refs.form.checkValidity()
            console.log(valid);

            if (!this.$refs.form.checkValidity()) {
                if (this.action.facilityId == '') {
                    this.action.facilityIDState = false;
                    return false;
                }      
                if (this.action.action == '') {
                    this.action.actionState = false;
                    return false;
                }    
            } else {
                return true;
            }
        },
        async registerAction() {
            this.startProcess();    
            this.$swal.fire({
                title: 'Would you like to insert product action?',
                showCancelButton: true,
                confirmButtonText: `Insert`,
                cancelButtonText: `Cancel`
            }).then(async (result) => {
                if (result.isConfirmed) {
                    this.startProcess();
                    try {
                        this.action.name = this.product.name;
                        let apiResult = await this.$http.post('/api/product/action', { params: [this.action] });
                        console.log(apiResult);                    a
                        if (apiResult.status == "200") {                        
                            this.$swal.fire('Success to insert', '', 'success');
                            this.endProcess(); 
                            this.loadActionHistory();        
                            this.$bvModal.hide('regist-new-action')                      
                        } else {
                            this.$swal.fire('Failed to insert!', apiResult, 'fail');
                            this.endProcess();   
                        }              
                    } catch (e) {
                        this.$swal.fire('Failed to insert!', e, 'fail');
                        this.endProcess();   
                    }                            
                } 
            });           
        }
    }
}
</script>