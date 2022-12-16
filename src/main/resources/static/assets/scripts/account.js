const { createApp } = Vue;

createApp({
    data() {
        return {
            id: "",
            datos: {},
            transacciones: [],
            idCuenta: "",
            numeroDeCuenta: "",
            fechaDeCreacion: "",
            balance: ""
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
                    this.datos = respuesta.data;
                    this.idCuenta = this.datos.id;
                    this.numeroDeCuenta = this.datos.number;
                    this.fechaDeCreacion = this.datos.creationDate.substring(2, 10).replaceAll("-", "/");
                    this.balance = this.datos.balance.toFixed(2);
                    this.transacciones = this.datos.transactions.sort((a, b) => a.id - b.id);
                    console.log(this.datos);
                })
                .catch(e => console.log(e));

        }
    },
    computed: {

    }
}).mount("#app");