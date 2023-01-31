const { createApp } = Vue;

createApp({
    data() {
        return {
            data: "",
            clientes: [],
            nombre: "",
            apellido: "",
            email: "",
            nombreReactivo: "",
            apellidoReactivo: "",
            emailReactivo: "",
            uri: "",
        }
    },
    created() {
        this.cargarDatos();
    },
    methods: {
        cargarDatos() {
            axios.get("/rest/clients")
                .then((respuesta) => {
                    console.log(respuesta)
                    this.data = respuesta.data;
                    this.clientes = respuesta.data._embedded.clients;
                    console.log(this.clientes)
                    this.uri = "/rest/clients/";
                })
                .catch(e => console.log(e));
        },

        agregarCliente() {
            if (this.nombre != "" && this.apellido != "" && this.email != "" && this.email.includes("@")) {
                this.enviarCliente(this.nombre, this.apellido, this.email);
                Swal.fire({
                    icon: "success",
                    text: "Customer added",
                })
            } else {
                Swal.fire({
                    icon: "error",
                    text: "Invalid values",
                });
            }

            this.nombre = "";
            this.apellido = "";
            this.email = "";
        },

        enviarCliente(nombre, apellido, email) {
            axios.post("/rest/clients", {
                firstName: nombre,
                lastName: apellido,
                email: email
            })
                .then(this.cargarDatos)
                .catch(e => console.log(e));
        },

        borrarCliente(cliente) {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.uri = this.uri + cliente.id;
                    axios.delete(this.uri)
                        .then(() => {
                            this.cargarDatos();
                            Swal.fire({
                                icon: "success",
                                text: "Deleted customer",
                            });
                        }).catch(e => console.log(e));                    
                }
            }).catch(e => console.log(e));
        },

        modificarCliente(cliente) {
            this.uri = this.uri + cliente.id;
            this.nombreReactivo = cliente.firstName;
            this.apellidoReactivo = cliente.lastName;
            this.emailReactivo = cliente.email;
        },

        confirmarModificacion() {
            if (this.nombreReactivo != "" && this.apellidoReactivo != "" && this.emailReactivo != "" && this.emailReactivo.includes("@")) {
                Swal.fire({
                    icon: "success",
                    text: "Modified client",
                });

                axios.put(this.uri, {
                    firstName: this.nombreReactivo,
                    lastName: this.apellidoReactivo,
                    email: this.emailReactivo
                })
                    .then(this.cargarDatos)
                    .catch(e => console.log(e));
            } else {
                Swal.fire({
                    icon: "error",
                    text: "Invalid values",
                });
            }
        }
    },
}).mount("#app");