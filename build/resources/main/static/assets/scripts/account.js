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
            cliente: {},
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
                    this.cliente = this.json.data;
                    this.cuenta = respuesta.data.account.find(cuenta => cuenta.id == this.id);
                    this.numeroDeCuenta = this.cuenta.number;
                    this.balance = this.cuenta.balance;
                    this.fechaDeCreacion = this.cuenta.creationDate.substring(0,10).replaceAll("-", "/");
                    this.transacciones = this.cuenta.transactions.sort((a, b) => a.id - b.id);
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

    }
}).mount("#app");