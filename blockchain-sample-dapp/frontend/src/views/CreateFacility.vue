<template>
    <div style="height:700px;">
        <div class="sticky-md-top text-center">
            <h1>Create Facility</h1>
            <hr class="border border-primary border-2 opacity-30">
        </div>      
        <div class="mb-3 row" style="padding:20px">
            <label for="facilityName" class="col-sm-2 col-form-label">Facility Name</label>
            <div class="col-sm-10">
            <input type="text" class="form-control" id="facilityName" v-model="facility.name">
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
            <button class="btn btn-primary me-md-2" type="button" @click="facilityInsert()" v-show="showButtons">Create</button>            
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
                name: "",
                location: "",
                telNo: "",
                category: "",
                emailAddress: ""
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
        facilityInsert() {          
            if (this.facility.name == "") {
              return  this.$swal('Missing', 'Please insert facility name', 'OK');
            }
            if (this.facility.location == "") {
              return  this.$swal('Missing', 'Please insert facility address', 'OK');
            }
            if(this.facility.telNo == "") {
              return this.$swal('Missing', 'Please insert facility telephone', 'OK');
            }
            if (this.facility.emailAddress == "") {
              return this.$swal('Missing', 'Please insert facility e-mail', 'OK');
            }
            if (this.facility.category == "") {
              return this.$swal('Missing', 'Please insert facility type', 'OK');
            }
            this.$swal.fire({
                title: 'Would you like to create facility?',
                showCancelButton: true,
                confirmButtonText: `Create`,
                cancelButtonText: `Cancel`
            }).then(async (result) => {
                if (result.isConfirmed) {
                    this.startProcess();
                    let apiResult = await this.$http.post('/api/facility', { params: [this.facility] });
                    console.log(apiResult);                    
                    if (apiResult.status == "200") {                        
                        this.$swal.fire('Success to create', '', 'success');
                        this.endProcess(); 
                        this.$router.push({ path: '/FacilityList' });                              
                    } else {
                        this.$swal.fire('Failed to create!', apiResult, 'fail');
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