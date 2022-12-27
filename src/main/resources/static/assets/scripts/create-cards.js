const { createApp } = Vue;

createApp({
    data(){
        return {
            cliente: {},
            typeCard: "",
            colorCard: ""
        }
    },
    created(){
        this.cargarDatos();
    },
    methods:{
        cargarDatos(){
            axios.get("http://localhost:8080/api/clients/current")
                .then(respuesta => {
                    this.cliente = respuesta.data;
                    console.log(this.cliente)
                })
                .catch(e => console.log(e));
        },

        solicitarTarjeta(){
            if(this.typeCard.length == 0 || this.colorCard.length == 0){
                
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Invalid fields!'
                  })

            }else{

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
                            text: "Your card has been requested.",
                        }).then(r => {
        
                            axios.post("http://localhost:8080/api/clients/current/cards", "type=" + this.typeCard + 
                                                                                          "&color=" + this.colorCard
                            ).then(r => {
                                location.href = "http://localhost:8080/web/cards.html";
                            })
                            .catch(e => console.log(e));
                        })
                    }
                }).catch(e => console.log(e));
            }
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

        },

    },
    computed:{

    }
}).mount("#app");