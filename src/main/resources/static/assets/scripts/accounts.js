const { createApp } = Vue;

createApp({
    data() {
        return {
            id: "",
            cliente: {},
            cuentas: {},
            prestamos: {},
        }
    },
    created() {
        this.cargarDatos();
    },
    methods: {
        cargarDatos() {
            this.id = new URLSearchParams(document.location.search).get("id");
            axios.get("http://localhost:8080/api/clients/" + this.id)
                .then(respuesta => {
                    this.cliente = respuesta.data;
                    this.cuentas = this.cliente.account;
                    this.prestamos = this.cliente.loans.sort((a, b) => a.id - b.id);
                    console.log(this.prestamos);
                })
                .catch(e => console.log(e));
        }
    }
}).mount("#app");