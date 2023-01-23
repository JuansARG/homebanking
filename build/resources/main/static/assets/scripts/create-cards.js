const { createApp } = Vue;

createApp({
    data() {
        return {
            cliente: {},
            typeCard: "",
            colorCard: "",
            tarjetas: [],
            tarjetasCredito: [],
            tarjetasDebito: [],
            cuentas: [],
            cuentaSeleccionada: ""
        }
    },
    created() {
        this.cargarDatos();
    },
    methods: {
        cargarDatos() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(r => {
                    this.cliente = r.data;
                    this.tarjetas = this.cliente.cards;
                    this.tarjetasCredito = this.tarjetas.filter(tarjeta => tarjeta.type == "CREDIT");
                    this.tarjetasDebito = this.tarjetas.filter(tarjeta => tarjeta.type == "DEBIT");
                    this.cuentas = this.cliente.account;
                    console.log(this.cuentas);
                })
                .catch(e => console.log(e));
        },
        solicitarTarjeta() {
            if (this.typeCard.length == 0 || this.colorCard.length == 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Invalid fields!'
                })
            } else {
                let fn = e => e.color;
                if(this.typeCard == "DEBIT"){
                    let coloresDebito = this.tarjetasDebito.map(fn);
                    if(coloresDebito.includes(this.colorCard)){
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'You already have that color for the type of card requested!'
                        })
                    }else{
                        this.confirmarCreacion();
                    }
                }
                if(this.typeCard == "CREDIT"){
                    let coloresCredito = this.tarjetasCredito.map(fn)
                    if(coloresCredito.includes(this.colorCard)){
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'You already have that color for the type of card requested!'
                        })
                    }else{
                        this.confirmarCreacion();
                    }
                }
            }
        },
        confirmarCreacion(){
            Swal.fire({
                title: 'Are you sure?',
                text: "",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!'
            }).then(r => {
                if (r.isConfirmed) {
                    axios.post("http://localhost:8080/api/clients/current/cards", 
                            `type=${this.typeCard}&color=${this.colorCard}&numberOfAccount=${this.cuentaSeleccionada}`)
                        .then(() => {
                            Swal.fire({
                                icon: "success",
                                text: "Your card has been requested.",
                            }).then(() => location.href = "http://localhost:8080/web/cards.html")
                            })
                        .catch(e => console.log(e));
                }
            }).catch(e => console.log(e));
        },
        logout() {
            Swal.fire({
                title: 'Are you sure?',
                text: "",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!'
            }).then(r => {
                if (r.isConfirmed) {
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
        }
    },
    computed: {
    }
}).mount("#app");