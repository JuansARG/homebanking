const { createApp } = Vue;

createApp({
    data() {
        return {
            json: "",
            cuenta: {},
            numeroDeCuenta: "",
            transacciones: [],
            fechaDeCreacion: "",
            balance: "",
            linkAccounts: ''
        }
    },
    created() {
        this.cargarDatos();
    },
    methods: {
        cargarDatos() {
            this.id = new URLSearchParams(document.location.search).get("id");
            axios.get("http://localhost:8080/api/accounts/" + this.id)
                .then((respuesta) => {
                    this.json = respuesta;
                    this.linkAccounts = document.referrer;
                    this.cuenta = respuesta.data;
                    this.numeroDeCuenta = this.cuenta.number;
                    this.transacciones = this.cuenta.transactions.sort((a, b) => a.id - b.id);
                    this.fechaDeCreacion = this.cuenta.creationDate.substring(0,10).replaceAll("-", "/");
                    this.balance = this.cuenta.balance;
                })
                .catch(e => console.log(e));
        }
    },
    computed: {

    }
}).mount("#app");