const { createApp} = Vue;

createApp({
    data(){
        return {
            json: "",
        }
    },
    created(){
        this.cargarDatos();
    },
    methods: {
        cargarDatos(){
            axios.get("http://localhost:8080/api/clients/1")
                .then(respuesta => {
                    this.json = respuesta;
                    console.log(this.json);
                })
                .catch(e => console.log(e));
        }
    }
}).mount("#app");