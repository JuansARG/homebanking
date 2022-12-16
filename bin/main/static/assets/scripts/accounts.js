const {createApp} = Vue;

createApp({
    data(){
        return{
            cuentas : {},
            cliente: {},
        }
    },
    created(){
        this.cargarDatos();
    },
    methods:{
        cargarDatos(){
            axios.get("http://localhost:8080/api/clients/1")
            .then(respuesta => {
                this.cliente = respuesta.data;
                this.cuentas = respuesta.data.account;
                          
            })
        }
    }
}).mount("#app");