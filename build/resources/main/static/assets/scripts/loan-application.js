const { createApp } = Vue;

createApp({
    data(){
        return {
            cliente: {},
            cuentas: [],
            numeroDeCuentaSeleccionada: "",
            tiposDePrestamos: [],
            nombrePrestamoSeleccionado: "",
            cuotasDisponibles: [],
            cuotasSeleccionadas: "",
            montoSolicitado: 0,
            montoMaximo: 0,
            montoMinimo: 0,
            intereses: 0,
        }
    },
    created(){
        this.cargarDatos();
    }, 
    methods:{
        cargarDatos(){
            axios.get("/api/clients/current")
                .then(r => {
                    this.cliente = r.data;
                    console.log(this.cliente);
                    this.cuentas = this.cliente.account;
                    axios.get("/api/loans")
                        .then(r => {
                            this.tiposDePrestamos = r.data;
                            console.log(this.tiposDePrestamos);
                        })
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        },
        solicitarPrestamo(){
            if(this.montoSolicitado < this.montoMinimo){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `The minimum amount is ${this.montoMinimo}.`,
                })
            } else if(this.nombrePrestamoSeleccionado == ""){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `You must select a type of loan.`,
                })
            } else if(this.cuotasSeleccionadas == ""){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `You must select a number of installments.`,
                })
            } else if(this.numeroDeCuentaSeleccionada == ""){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `You must select a destination account.`,
                })
            }else{
                Swal.fire({
                    title: 'Are you sure?',
                    text: "",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Yes!'
                }).then(r => {
                    if(r.isConfirmed){
                        axios({
                            method: "post",
                            url: "/api/loans",
                            data: {
                                idOfLoan: this.tiposDePrestamos.find(p => p.name == this.nombrePrestamoSeleccionado).id,
                                amount: this.montoSolicitado,
                                payments: this.cuotasSeleccionadas,
                                destinationAccountNumber: this.numeroDeCuentaSeleccionada
                            }
                        })
                        .then(() => {
                            Swal.fire({
                                icon: "success",
                                text: "Your loan has been requested.",
                            })
                            .then(() => location.href = "/web/accounts.html")
                            .catch(e => console.log(e));
                        })
                        .catch(e => {
                            console.log(e);
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: e.response.data,
                            })
                        });
                    }
                })
            }
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
                        axios.post("/api/logout")
                            .then(() => location.href = "/web/login.html")
                            .catch(e => console.log(e));
                    })
                }
            }).catch(e => console.log(e));
        }
    },
    computed:{
        calcularCuotasMontoMaxMontoMin(){
            switch(this.nombrePrestamoSeleccionado){
                case "Mortgage Loan":
                    this.cuotasDisponibles = this.tiposDePrestamos.find(prestamo => prestamo.name == "Mortgage Loan").payments;
                    this.montoMaximo = this.tiposDePrestamos.find(prestamo => prestamo.name == "Mortgage Loan").maxAmount;
                    this.montoMinimo = 300000;
                    this.intereses = this.tiposDePrestamos.find(prestamo => prestamo.name == "Mortgage Loan").interests;
                    break;
                case "Personal Loan": 
                    this.cuotasDisponibles = this.tiposDePrestamos.find(prestamo => prestamo.name == "Personal Loan").payments;
                    this.montoMaximo = this.tiposDePrestamos.find(prestamo => prestamo.name == "Personal Loan").maxAmount;
                    this.montoMinimo = 25000;
                    this.intereses = this.tiposDePrestamos.find(prestamo => prestamo.name == "Personal Loan").interests;
                    break;
                case "Car Loan": 
                    this.cuotasDisponibles = this.tiposDePrestamos.find(prestamo => prestamo.name == "Car Loan").payments;
                    this.montoMaximo = this.tiposDePrestamos.find(prestamo => prestamo.name == "Car Loan").maxAmount;
                    this.montoMinimo = 100000;
                    this.intereses = this.tiposDePrestamos.find(prestamo => prestamo.name == "Car Loan").interests;
                    break;
            }
        }
    }
}).mount("#app");
