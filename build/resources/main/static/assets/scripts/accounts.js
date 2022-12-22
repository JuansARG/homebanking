const { createApp } = Vue;

createApp({
    data() {
        return {
            email: "",
            cliente: {},
            cuentas: {},
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
        }
    }
}).mount("#app");