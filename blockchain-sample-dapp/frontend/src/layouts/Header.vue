<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="height:90px">
    <div class="container-fluid">
      <a class="navbar-brand" >Tracking</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="nav-link" to="/Dashboard">Dashboard</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/FacilityList">Facility</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/ProductList">Product</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script>
export default {
  name: 'Header'
}
</script>