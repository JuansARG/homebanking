const { createApp} = Vue;

createApp({
    data(){
        return {
            json: {},
            cliente: {},
            tipoTransferencia: "",
            cuentas: [],
            cuentaOrigen: "",
            cuentaDestino: "",
            cuentasRestantes: [],
            cuentaTarget: {},
            balanceCuentaTarget: 0,
            montoTransferencia: 0,
            descripcion: "",
        }
    },
    created(){
        this.cargarDatos();
    },
    methods: {
        cargarDatos(){
            axios.get("/api/clients/current")
                .then(r => {
                    this.json = r;
                    this.cliente = this.json.data;
                    this.cuentas = this.cliente.account;  
                })
                .catch(e => console.log(e));
        },

        realizarTransacion(){
                Swal.fire({
                    title: 'Are you sure?',
                    text: `Root account: ${this.cuentaOrigen} | Destination account: ${this.cuentaDestino} | Amount:  ${this.montoTransferencia}`,
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Yes!'
                }).then(r => {
                    if(r.isConfirmed){
                        axios.post("/api/transactions", 
                                `amount=${this.montoTransferencia}&description=${this.descripcion}&numberRootAccount=${this.cuentaOrigen}&numberDestinationAccount=${this.cuentaDestino}`
                                ).then(() => {
                                    Swal.fire({
                                        icon: "success",
                                        text: "The transfer has been successful, the changes can already be seen reflected in the accounts section, it will be redirected there.",
                                    }).then(() => location.href = "/web/accounts.html") 
                                })
                                .catch(e => {
                                    Swal.fire({
                                        icon: 'error',
                                        title: 'Oops...',
                                        text: e.response.data
                                    })
                                });
                    }
                }).catch(e => console.log(e));
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
                        axios.post("/api/logout")
                        .then(() => location.href = "/web/login.html")
                        .catch(e => console.log(e));
                    })
                }
            }).catch(e => console.log(e));
        }
    },
    computed: {
        mostrarCuentasRestantes(){
            this.cuentasRestantes = this.cuentas.filter(cuenta => cuenta.number != this.cuentaOrigen);
            this.cuentaTarget = this.cuentas.find(cuenta => cuenta.number == this.cuentaOrigen);
            if(this.cuentaTarget != undefined){
                this.balance = this.cuentaTarget.balance;
            }
        }
    }
}).mount("#app");