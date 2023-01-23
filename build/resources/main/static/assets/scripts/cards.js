const { createApp} = Vue;

createApp({
    data(){
        return {
            json: {},
            cliente: {},
            tarjetas: [],
            tarjetasCredito: [],
            tarjetasDebito: [],
            fechaActual: "",
        }
    },
    created(){
        this.cargarDatos();
    },
    methods: {
        cargarDatos(){
            axios.get("http://localhost:8080/api/clients/current")
                .then(r => {
                    this.json = r;
                    this.cliente = this.json.data;
                    this.tarjetas = this.cliente.cards;
                    this.tarjetasCredito = this.tarjetas.filter(tarjeta => tarjeta.type == "CREDIT");
                    this.tarjetasDebito = this.tarjetas.filter(tarjeta => tarjeta.type == "DEBIT");
                    this.avisoDeVencimiento();
                    console.log(this.tarjetas);
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
                    }).then(() => {
                        axios.post("http://localhost:8080/api/logout")
                            .then(() => location.href = "http://localhost:8080/web/login.html")
                            .catch(e => console.log(e));
                    })
                }
            }).catch(e => console.log(e));
        },

        borrarTarjeta(id){
            Swal.fire({
                title: 'Are you sure to delete the card?',
                text: "",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!'
            }).then(r => {
                if(r.isConfirmed){
                    axios({
                        method: 'delete',
                        url: `http://localhost:8080/api/clients/current/cards/${id}`
                    }).then(() => {
                        Swal.fire({
                            icon: "success",
                            text: "The card has been deleted.",
                        }).then(() => location.href = 'http://localhost:8080/web/cards.html');
                    }).catch(e => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: e.response.data
                        })
                    })
                }
            })
        },

        avisoDeVencimiento(){
            let numerosDeTarjetasVencidas = [...this.tarjetas].filter(tarjeta => {
                if(tarjeta.fromDate < this.fechaActual){
                    return tarjeta.number;
                }
            });
            console.log(numerosDeTarjetasVencidas);
            if(numerosDeTarjetasVencidas[0] != undefined){
                let msg = "";
                if(numerosDeTarjetasVencidas.length > 1){
                    msg = `The cards with the following numbers are expired: | `
                    numerosDeTarjetasVencidas.forEach(t => {
                        msg += t.number + " | ";
                    });
                    this.lanzarAviso(msg);
                }
                if(numerosDeTarjetasVencidas.length == 1){
                    msg = `Card number ${numerosDeTarjetasVencidas[0].number} is expired.`
                    this.lanzarAviso(msg);
                }
            }
        },
        
        lanzarAviso(msg){
            setTimeout(() => {
                Swal.fire({
                    icon: 'warning',
                    title: 'Warning!',
                    text: msg
                })
            }, 2500);
        }
    },
    computed:{
        calcularFecha(){
            this.fechaActual = new Date();
            this.fechaActual = this.fechaActual.getFullYear() + "-" + (this.fechaActual.getMonth() + 1) + "-" + this.fechaActual.getDate();
            console.log(this.fechaActual);
        }
    }
}).mount("#app");