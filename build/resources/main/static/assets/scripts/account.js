const { createApp } = Vue;

createApp({
    data() {
        return {
            json: "",
            id: undefined,
            cuenta: {},
            numeroDeCuenta: "",
            transacciones: [],
            fechaDeCreacion: "",
            balance: "",
        }
    },
    created() {
        this.cargarDatos();
    },
    methods: {
        cargarDatos() {
            this.id = new URLSearchParams(document.location.search).get("id");
            axios.get("http://localhost:8080/api/clients/current")
                .then((respuesta) => {
                    this.json = respuesta;
                    this.cuenta = respuesta.data.account.find(cuenta => cuenta.id = this.id);
                    this.numeroDeCuenta = this.cuenta.number;
                    this.transacciones = this.cuenta.transactions.sort((a, b) => a.id - b.id);
                    this.fechaDeCreacion = this.cuenta.creationDate.substring(0,10).replaceAll("-", "/");
                    this.balance = this.cuenta.balance;
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
    },
    computed: {

    }
}).mount("#app");