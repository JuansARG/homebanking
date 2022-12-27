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
                .then(respuesta => {
                    this.cliente = respuesta.data;
                    this.cuentas = this.cliente.account;
                    this.prestamos = this.cliente.loans.sort((a, b) => a.id - b.id);
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

        crearCuenta(){
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
                        text: "Your account has been created.",
                    }).then(r => {
    
                        axios.post("http://localhost:8080/api/clients/current/accounts")
                        .then(r => {
                            location.reload();
                        })
                        .catch(e => console.log(e));
                    })
                } 
            }).catch(e => {

            });
        }
    },
}).mount("#app");