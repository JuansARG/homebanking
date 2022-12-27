const { createApp} = Vue;

createApp({
    data(){
        return {
            json: {},
            cliente: {},
            tarjetas: []
        }
    },
    created(){
        this.cargarDatos();
    },
    methods: {
        cargarDatos(){
            axios.get("http://localhost:8080/api/clients/current")
                .then(respuesta => {
                    this.json = respuesta;
                    this.cliente = this.json.data;
                    this.tarjetas = this.cliente.cards;
                })
                .catch(e => console.log(e));
        },

        logout(){
            axios.post("http://localhost:8080/api/logout")
                .then(r => {
                    alert("redirect to login");
                    location.href = "http://localhost:8080/web/login.html"
                })
                .catch(e => console.log(e));
        },
        /*
        crearTarjeta(){
            axios.post('http://localhost:8080/api/clients/current/card', "type=" + this.tipoTarjeta +
                                                                        "&color=" + this.colorTarjeta)
            .then(r => {
                location.reload();
            })
            .catch(e => console.log(e));
        }
        */
    }
}).mount("#app");