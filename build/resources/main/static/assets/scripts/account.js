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
            axios.get("/api/clients/current")
                .then(respuesta => {
                    this.json = respuesta;
                    this.cliente = this.json.data;
                    this.cuenta = respuesta.data.account.find(cuenta => cuenta.id == this.id);
                    this.numeroDeCuenta = this.cuenta.number;
                    this.balance = this.cuenta.balance.toFixed(2);
                    this.fechaDeCreacion = this.cuenta.creationDate.substring(0,10).replaceAll("-", "/");
                    this.transacciones = this.cuenta.transactions.sort((a, b) => b.id - a.id);
                    console.log(this.transacciones);
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
                        axios.post("/api/logout")
                            .then(() => location.href = "/web/login.html")
                            .catch(e => console.log(e));
                    })
                }
            }).catch(e => console.log(e));

        }
    },
    computed: {

    }
}).mount("#app");