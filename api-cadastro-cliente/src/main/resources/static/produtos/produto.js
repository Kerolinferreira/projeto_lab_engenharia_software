document.addEventListener('DOMContentLoaded', () => {
    
    // --- Referências do DOM ---
    const form = document.getElementById('produtoForm');
    const submitButton = document.getElementById('submitButton');
    const btnGerar = document.querySelector('.btn-gerar');

    // Campos para a IA
    const inpNome = document.getElementById('nome_produto');
    const inpModelo = document.getElementById('modelo');
    const inpTamanho = document.getElementById('tamanho');
    const inpCor = document.getElementById('cor');
    const txtDescricao = document.getElementById('descricao');

    if (!form || !submitButton) {
        console.error('Formulário ou botão de submit não encontrado.');
        return;
    }

    // --- Lógica de Envio do Formulário Principal ---
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        // 1. Validação do HTML5
        if (!form.checkValidity()) {
            form.reportValidity(); 
            return; 
        }

        // 2. Coletar dados do formulário
        const formData = new FormData(form);
        const dadosPlanos = Object.fromEntries(formData.entries());
        
        // 3. Montar o DTO para o backend (API Java)
        const dadosParaApi = {
            nome_produto: dadosPlanos.nome_produto,
            descricao: dadosPlanos.descricao,
            modelo: dadosPlanos.modelo || null, // Trata campo opcional
            qtd_estoque: parseInt(dadosPlanos.qtd_estoque, 10),
            preco: parseFloat(dadosPlanos.preco)
        };

        // 4. Desabilitar botão e enviar
        submitButton.disabled = true;
        submitButton.textContent = 'Salvando...';

        try {
            // Ajuste o endpoint '/api/produtos' se necessário.
            const response = await fetch('/api/produtos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                cache: 'no-store',
                body: JSON.stringify(dadosParaApi)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `Erro ${response.status}`);
            }

            // 5. Sucesso
            alert('Produto cadastrado com sucesso!');
            form.reset(); // Limpa o formulário

        } catch (error) {
            console.error('Falha ao enviar o formulário:', error);
            alert(`Erro no cadastro: ${error.message}`);
        } finally {
            // 6. Reabilitar o botão
            submitButton.disabled = false;
            submitButton.textContent = 'SALVAR PRODUTO';
        }
    });


    // --- Lógica (Stub) para Gerar Descrição (IA) ---
    if (btnGerar) {
        btnGerar.addEventListener('click', async () => {
            
            const nome = inpNome.value;
            const modelo = inpModelo.value;
            const tamanho = inpTamanho.value;
            const cor = inpCor.value;

            if (!nome) {
                alert('Preencha pelo menos o Nome do Produto para gerar a descrição.');
                inpNome.focus();
                return;
            }

            // Texto de "carregando"
            txtDescricao.value = "Gerando descrição com IA, aguarde...";
            btnGerar.disabled = true;

            try {
                // ==========================================================
                // Fetch pra gerar ia
                // Exemplo (descomente e ajuste o endpoint):
                /*
                const payloadIa = { nome, modelo, tamanho, cor };
                const response = await fetch('/api/ia/gerar-descricao', {
                     method: 'POST',
                     headers: { 'Content-Type': 'application/json' },
                     body: JSON.stringify(payloadIa)
                });
                
                if (!response.ok) throw new Error('Falha ao gerar descrição');
                
                const data = await response.json();
                const textoGerado = data.descricao; // ou data.texto, ajuste conforme sua API
                */
                // ==========================================================
                
                // STUB (simulação) APAGAR DPS !!!!!!!!!!!
                await new Promise(r => setTimeout(r, 1500)); // Simula delay da rede
                const textoGerado = `Este é um ${nome} modelo ${modelo || 'padrão'}, no tamanho ${tamanho || 'único'} e cor ${cor || 'indefinida'}. Perfeito para quem busca conforto e estilo... (Texto gerado por IA)`;
                // Fim do STUB

                // Preenche o textarea com a resposta
                txtDescricao.value = textoGerado;

            } catch (error) {
                console.error('Erro ao gerar descrição:', error);
                alert(error.message);
                // Limpa o campo se der erro
                txtDescricao.value = dadosPlanos.descricao || ''; 
            } finally {
                btnGerar.disabled = false;
            }
        });
    }
});