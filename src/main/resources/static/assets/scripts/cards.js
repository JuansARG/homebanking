const { createApp} = Vue;

createApp({
    data(){
        return {
            id: "",
            json: {},
            cliente: {},
            tarjetas: [],
        }
    },
    created(){
        this.cargarDatos();
    },
    methods: {
        cargarDatos(){
            this.id = new URLSearchParams(document.location.search).get("id");
            axios.get("http://localhost:8080/api/clients/" + this.id)
                .then(respuesta => {
                    this.json = respuesta;
                    this.cliente = this.json.data;
                    this.tarjetas = this.cliente.cards;
                })
                .catch(e => console.log(e));
        }
    }
}).mount("#app");