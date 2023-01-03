const { createApp} = Vue;

createApp({
    data(){
        return {
            json: {},
            cliente: {},
            tarjetas: [],
            tarjetasCredito: [],
            tarjetasDebito: [],
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
                    this.tarjetasCredito = this.tarjetas.filter(tarjeta => tarjeta.type == "CREDIT");
                    this.tarjetasDebito = this.tarjetas.filter(tarjeta => tarjeta.type == "DEBIT")                 
                })
                .catch(e => console.log(e));
                
        },

        logout(){

            Swal.fire({
                title: 'Are you sure?',
                text: "",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!'
            }).then(r => {
                if(r.isConfirmed){
                    Swal.fire({
                        icon: "success",
                        text: "Will be redirected, see you soon.",
                    }).then(r => {
                        axios.post("http://localhost:8080/api/logout")
                        .then(() => location.href = "http://localhost:8080/web/login.html")
                        .catch(e => console.log(e));
                    })
                }
            }).catch(e => console.log(e));

        }
    }
}).mount("#app");