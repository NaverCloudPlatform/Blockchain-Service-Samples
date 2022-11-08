<template>
   <div style="height:700px;">
     <div class="sticky-md-top text-center">
        <h1>Dashboard</h1>
        <hr class="border border-primary border-2 opacity-30">
     </div>

      <div class="container text-center">
        <div class="row g-4">
            <div class="col-12">
            <div class="p-3 border bg-light"><h1 class="display-6">Facility Count : {{facilityCount}}</h1>  </div>
            </div>
            <div class="col-12">
            <div class="p-3 border bg-light"><h1 class="display-6">Product Count : {{productCount}}</h1></div>
            </div>
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
            facilityCount: 0,
            productCount: 0,
            totalTransactionCount: 0,
            totalBlockCount: 0
        };
    },
    setup() {},
    created() {
        this.getDashboardInfo();
    },
    mounted() {},
    unmounted() {},
    methods: {
        async getDashboardInfo() {            
            let facilityCount = await this.$http.get('/api/dashboard/facility/count');
            this.facilityCount = facilityCount.data;       
            console.log(facilityCount);
            let productCount = await this.$http.get('/api/dashboard/product/count');
            this.productCount = productCount.data;            
        }      
    }
}
</script>

