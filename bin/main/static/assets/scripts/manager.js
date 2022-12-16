const {createApp} = Vue;

createApp({
    data(){
        return{
            datos: undefined,
            clientes:undefined,
            nombre: "",
            apellido: "",
            email: "",
            nombreReactivo: "",
            apellidoReactivo: "",
            emailReactivo: "",
            uri: "",
        }
    },
    created(){
        this.cargarDatos();
    },
    methods:{

        cargarDatos(){
            axios.get("http://localhost:8080/rest/clients")
            .then((respuesta)=>{
                this.datos = respuesta.data;
                this.clientes = respuesta.data._embedded.clients;
            })
            .catch(e => console.log(e));
        },

        agregarCliente(){
            if(this.nombre != "" && this.apellido != "" && this.email !="" && this.email.includes("@")){
                this.enviarCliente(this.nombre, this.apellido, this.email);
                Swal.fire({
                    icon: "success",
                    text: "Customer added",
                    confirmButtonColor: "#F5AE53"
                })
            }else{
                Swal.fire({
                    icon: "error",
                    text: "Invalid values",
                    confirmButtonColor: "#F5AE53"
                });
            }

            
            this.nombre = "";
            this.apellido = "";
            this.email = "";
        },

        enviarCliente(nombre, apellido, email){
            axios.post("http://localhost:8080/rest/clients", {
                firstName: nombre,
                lastName: apellido,
                email: email
            })
            .then(this.cargarDatos)
            .catch(e => console.log(e));
        },

        borrarCliente(cliente){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#F5AE53',
                cancelButtonColor: '#31087B',
                confirmButtonText: 'Yes, delete it!'
              }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        icon: "success",
                        text: "Deleted customer",
                        confirmButtonColor: "#F5AE53"
                    });
                }
              })
           

            this.uri = cliente._links.self.href
            axios.delete(this.uri)
            .then(this.cargarDatos)
            .catch(e => console.log(e));
        },

        modificarCliente(cliente){
            this.uri = cliente._links.self.href
            this.nombreReactivo = cliente.firstName;
            this.apellidoReactivo = cliente.lastName;
            this.emailReactivo = cliente.email;
        },

        confirmarModificacion(){
            if(this.nombreReactivo != "" && this.apellidoReactivo != "" && this.emailReactivo !="" && this.emailReactivo.includes("@")){
                Swal.fire({
                    icon: "success",
                    text: "Modified client",
                    confirmButtonColor: "#F5AE53"
                });

                axios.put(this.uri, {
                    firstName: this.nombreReactivo,
                    lastName: this.apellidoReactivo,
                    email: this.emailReactivo
                })
                .then(this.cargarDatos)
                .catch(e => console.log(e));
            }else{
                Swal.fire({
                    icon: "error",
                    text: "Invalid values",
                    confirmButtonColor: "#F5AE53"
                });
            }
            }

    },
    
}).mount("#app");