const { createApp } = Vue;

createApp({
    data() {
        return {
            email: "",
            cliente: {},
            cuentas: {},
            prestamos: {},
            idPrestamo: 0, 
            cuenta: "",
            limiteDeCuotas: 0,
            cuotasParaPagar: 0,
            valorDeCuota: 0,
            pagoTotal: 0,
        }
    },
    created() {
        this.cargarDatos();
    },
    methods: {
        cargarDatos() {;
            axios.get("/api/clients/current")
                .then(respuesta => {
                    this.cliente = respuesta.data;
                    this.cuentas = this.cliente.account;
                    this.prestamos = this.cliente.loans.sort((a, b) => a.id - b.id);
                })
                .catch(e => console.log(e));
        },

        pagarCuotas(){
            axios.put("/api/loans", `accountNumber=${this.cuenta}&idCurrentClientLoan=${this.idPrestamo}&dues=${this.cuotasParaPagar}&value=${this.valorDeCuota}`)
                    .then(r => {
                        console.log(r);
                        Swal.fire({
                            icon: "success",
                            text: r.data,
                        }).then(() =>{
                            Swal.fire({
                                icon: "success",
                                text: "The page will reload to display the changes..",
                            }).then(() => location.href = "/web/accounts.html")
                        })
                    })
                    .catch(e => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: e.response.data,
                        })
                    })
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
        },

        crearCuenta(){
            Swal.fire({
                title: 'Select type account',
                input: 'select',
                inputOptions: {
                    CURRENT: 'Current',
                    SAVING: 'Saving'
                },
                inputPlaceholder: 'Select type',
                showCancelButton: true,
                inputValidator: (value) => {
                    return new Promise(() => {
                        if(value == ""){
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: 'Invalid type!'
                            })
                        }else{
                            Swal.fire({
                                title: 'Are you sure?',
                                text: `Are you sure you want to create a ${value} type account?`,
                                icon: 'warning',
                                showCancelButton: true,
                                confirmButtonText: 'Yes!'
                            }).then(r => {
                                if(r.isConfirmed){
                                    Swal.fire({
                                        icon: "success",
                                        text: "Your account has been created.",
                                    }).then(() => {
                                        console.log(value);
                                        axios.post("/api/clients/current/accounts", `type=${value}`)
                                        .then(() => location.reload())
                                        .catch(e => console.log(e));
                                    })
                                } 
                            }).catch(e => console.log(e));
                        }
                        
                    })
                }
            })
        },

        modificarValores(limiteDeCuotas, valorDeCuota, idPrestamo){
            this.limiteDeCuotas = limiteDeCuotas;
            this.valorDeCuota = valorDeCuota;
            this.idPrestamo = idPrestamo;
        },

        borrarCuenta(id){
            Swal.fire({
                title: 'Are you sure?',
                text: "You are about to delete this account, keep in mind that it must not have a balance to be deleted.",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!'
            }).then(r =>{
                if(r.isConfirmed){
                    axios.delete(`/api/clients/current/accounts/${id}`)
                    .then(r => {
                        console.log(r)
                        Swal.fire({
                            icon: "success",
                            text: r.data,
                        }).then(() => location.href = "/web/accounts.html")
                    })
                    .catch(e => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: e.response.data,
                        })
                    })
                }
            })
        }
    },
    computed:{
        calcularTotal(){
            this.pagoTotal = this.cuotasParaPagar * this.valorDeCuota;
        }
    }
}).mount("#app");