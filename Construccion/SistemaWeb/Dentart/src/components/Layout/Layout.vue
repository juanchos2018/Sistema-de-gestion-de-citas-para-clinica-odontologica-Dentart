<template>
<div :class="[{root: true, sidebarClose, sidebarStatic}, 'sing-dashboard']">
  <Sidebar />

  <div class="wrap">
   <Header   v-bind:ru="ru"  v-bind:name="nomb" />
    <v-touch class="content" @swipe="handleSwipe" :swipe-options="{direction: 'horizontal'}">
      <breadcrumb-history></breadcrumb-history>
      <transition name="router-animation">
        <router-view />
      </transition>
      <footer class="contentFooter">
        <!--
        Sing App Vue Dashboard Free Template - Made by <a href="https://flatlogic.com" rel="nofollow noopener noreferrer" target="_blank">Flatlogic</a>
          -->
          <div class="d-flex flex-row bd-highlight mb-3">        
          <h5 class="p-2 bd-highlight">Sistema - Aveo </h5>
          <h5 class="p-2 bd-highlight">Base de Datos :</h5>
          <h5 class="p-2 bd-highlight" > {{base}} </h5>       
          
         </div> 
        </footer>
    </v-touch>
  </div>
</div>
</template>

<script>
import axios from  'axios'
import { createNamespacedHelpers } from 'vuex';
const { mapState, mapActions } = createNamespacedHelpers('layout');

import Sidebar from '@/components/Sidebar/Sidebar';
import Header from '@/components/Header/Header';

import BreadcrumbHistory from '@/components/BreadcrumbHistory/BreadcrumbHistory';

import './Layout.scss';

export default {
  name: 'Layout',
  components: { Sidebar, Header,  BreadcrumbHistory },
  data(){
    return {
      nomb:'',
      ru:'',
      ruta_img:'',
      base:''
    }
  },
  methods: {
    ...mapActions(['switchSidebar', 'handleSwipe', 'changeSidebarActive', 'toggleSidebar'],
    ),
    handleWindowResize() {
      const width = window.innerWidth;

      if (width <= 768 && this.sidebarStatic) {
        this.toggleSidebar();
        this.changeSidebarActive(null);
      }
    },
    DatosEmpresa(){
       axios({
                method: 'get',
                url: 'Entidad/GetEntidad',
                data: {}
            })
            .then(response => {               
               this.nomb=response.data.nombre;  
               this.ru=response.data.ruc;
               this.ruta_img=response.logo                             
        })
    },
    DatosBd(){
        axios({
                method: 'get',
                url: 'Entidad/GetDB',
                data: {}
            })
            .then(response => {        
               
               this.base=response.data[0].nombre;  
                                      
        })
    }

  },
  computed: {
    ...mapState(["sidebarClose", "sidebarStatic"]),
  },
  created() {
    const staticSidebar = JSON.parse(localStorage.getItem('sidebarStatic'));

    if (staticSidebar) {
      this.$store.state.layout.sidebarStatic = true;
    } else if (!this.sidebarClose) {
      setTimeout(() => {
        this.switchSidebar(true);
        this.changeSidebarActive(null);
      }, 2500);
    }

    this.handleWindowResize();
    window.addEventListener('resize', this.handleWindowResize);
     this.DatosEmpresa();
     this.DatosBd();
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleWindowResize);
  }
};
</script>

<style src="./Layout.scss" lang="scss" />
