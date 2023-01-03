const { createApp } = Vue;

createApp({
    data() {
        return {
            email: "",
            cliente: {},
            cuentas: {},
            idCuenta : undefined,
            prestamos: {},
        }
    },
    created() {
        this.cargarDatos();
    },
    methods: {
        cargarDatos() {;
            axios.get("http://localhost:8080/api/clients/current")
                .then(() => {
                    this.cliente = respuesta.data;
                    this.cuentas = this.cliente.account;
                    this.prestamos = this.cliente.loans.sort((a, b) => a.id - b.id);
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

        crearCuenta(){
            Swal.fire({
                title: 'Are you sure?',
                text: "You are going to create an account...",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!'
            }).then(r => {
                if(r.isConfirmed){
                    Swal.fire({
                        icon: "success",
                        text: "Your account has been created.",
                    }).then(r => {
                        axios.post("http://localhost:8080/api/clients/current/accounts")
                        .then(() => location.reload())
                        .catch(e => console.log(e));
                    })
                } 
            }).catch(e => console.log(e));
        }
    },
}).mount("#app");