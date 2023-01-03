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

            axios.get("http://localhost:8080/api/clients/current")
                .then(respuesta => {
                    this.json = respuesta;
                    this.cliente = this.json.data;
                    this.cuentas = this.cliente.account;  
                })
                .catch(e => console.log(e));
        },

        realizarTransacion(){

            this.cuentaDestino = this.cuentaDestino.toUpperCase();
            if(this.montoTransferencia > this.cuentaTarget.balance){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Invalid amount!'
                })
            }else {
                Swal.fire({
                    title: 'Are you sure?',
                    text: `Root account: ${this.cuentaOrigen} | Destination account: ${this.cuentaDestino} | Amount:  ${this.montoTransferencia}`,
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Yes!'
                }).then(r => {
                    if(r.isConfirmed){
                        Swal.fire({
                            icon: "success",
                            text: "The transfer has been successful, the changes can already be seen reflected in the accounts section, it will be redirected there.",
                        }).then(() => {
                            axios.post("http://localhost:8080/api/transactions", 
                                    `amount=${this.montoTransferencia}&description=${this.descripcion}&numberRootAccount=${this.cuentaOrigen}&numberDestinationAccount=${this.cuentaDestino}`
                            ).then(() => location.href = "http://localhost:8080/web/accounts.html")
                            .catch(e => console.log(e));
                        });    
                    }
                }).catch(e => console.log(e));

            }

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
                        .then(r => {
                            location.href = "http://localhost:8080/web/login.html"
                        })
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