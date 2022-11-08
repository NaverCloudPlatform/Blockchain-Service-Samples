<template>
    <div style="height:700px;">
        <div class="sticky-md-top text-center">
            <h1>View Facility</h1>
            <hr class="border border-primary border-2 opacity-30">
        </div>  
        <div class="mb-3 row" style="padding:20px">
            <label for="facilityName" class="col-sm-2 col-form-label">Facility ID</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="facilityName" v-model="facility.id" disabled>
            </div>
        </div>    
        <div class="mb-3 row" style="padding:20px">
            <label for="facilityName" class="col-sm-2 col-form-label">Facility Name</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="facilityName" v-model="facility.name" disabled>
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="address" class="col-sm-2 col-form-label">Address</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="address" v-model="facility.location">
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="telephone" class="col-sm-2 col-form-label">Telephone</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="telephone" v-model="facility.telNo">
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="email" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="email" placeholder="please insert like id@example.com" v-model="facility.emailAddress">
            </div>
        </div>
        <div class="mb-3 row" style="padding:20px">
            <label for="type" class="col-sm-2 col-form-label">Facility Category</label>
            <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" id="type" v-model="facility.category">
                    <option value="farm">Farm</option>
                    <option value="Warehouse">Warehouse</option>
                    <option value="Logist">Logist</option>
                    <option value="Wholesale">Wholsale</option>
                    <option value="Retail">Retail</option>
                </select>
            </div>
        </div>
        <div class="d-grid gap-4 d-md-flex justify-content-md-end col-7" >
            <button class="btn btn-primary me-md-2" type="button" @click="insertFacility()" v-show="showButtons">Update</button>            
            <button class="btn btn-primary me-md-2" type="button" @click="deleteFacility()" v-show="showButtons">Delete</button>            
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
            facility: {
                id: "",
                name: "",
                emailAddress: "",
                telNo: "",
                category: "",
                location: ""
            },
            showButtons: true,
            showSpinner: false
        };
    },
    setup() {},
    created() {        
        this.facility.id = this.$route.query.facilityId;
        this.init();
    },
    mounted() {},
    unmounted() {},
    methods: {
        async init() {            
            const result = await this.$http.get('/api/facility/'+this.facility.id);            
            this.facility = JSON.parse(JSON.stringify(result.data));
            this.facility.id = this.$route.query.facilityId;
            console.log(this.facility);            
        },
        goBack() {
            this.$router.go(-1);
        },
        insertFacility() {          
            if (this.facility.name == "") {
              return  this.$swal('Missing', 'Please insert facility name', 'OK');
            }
            if (this.facility.address == "") {
              return  this.$swal('Missing', 'Please insert facility address', 'OK');
            }
            if(this.facility.telephone == "") {
              return this.$swal('Missing', 'Please insert facility telephone', 'OK');
            }
            if (this.facility.email == "") {
              return this.$swal('Missing', 'Please insert facility e-mail', 'OK');
            }
            if (this.facility.type == "") {
              return this.$swal('Missing', 'Please insert facility type', 'OK');
            }
            this.$swal.fire({
                title: 'Would you like to update facility?',
                showCancelButton: true,
                confirmButtonText: `Update`,
                cancelButtonText: `Cancel`
            }).then(async (result) => {
                if (result.isConfirmed) {
                    this.startProcess();
                    let apiResult = await this.$http.put('/api/facility', { params: [this.facility] });
                    console.log(apiResult);                    
                    if (apiResult.status == "200") {                        
                        this.$swal.fire('Success to update', '', 'success');
                        this.endProcess(); 
                        this.$router.push({ path: '/FacilityList' });                              
                    } else {
                        this.$swal.fire('Failed to update!', apiResult, 'fail');
                        this.endProcess();   
                    }                    
                } 
            });
        },
        deleteFacility() {    
            this.$swal.fire({
                title: 'Are you sure to delete this facility?',
                showCancelButton: true,
                confirmButtonText: `Delete`,
                cancelButtonText: `Cancel`
            }).then(async (result) => {
                if (result.isConfirmed) {
                    this.startProcess();
                    let apiResult = await this.$http.delete('/api/facility/'+this.facility.id);
                    console.log(apiResult);                    
                    if (apiResult.status == "200") {                        
                        this.$swal.fire('Success to delete', '', 'success');
                        this.endProcess(); 
                        this.$router.push({ path: '/FacilityList' });                              
                    } else {
                        this.$swal.fire('Failed to delete!', apiResult, 'fail');
                        this.endProcess();   
                    }                    
                } 
            });
        },
        startrocess() {
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