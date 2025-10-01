const inpCep = document.getElementById('cep');
const inpLogradouro = document.getElementById('logradouro');
const inpNumero = document.getElementById('numero');
const inpBairro = document.getElementById('bairro');
const inpCidade = document.getElementById('cidade');
const inpUf = document.getElementById('uf');
const slcTipo = document.getElementById('tipo');
const inpCpfCnpj = document.getElementById('cpf_cnpj');
const inpNome = document.getElementById('nome');
const inpDataNasc = document.getElementById('data_nasc');


const preencherCamposComDadosCnpj = (dadosCnpj) => {
    inpNome.value = dadosCnpj.razao_social || '';
    inpDataNasc.value = dadosCnpj.estabelecimento.data_inicio_atividade || '';
};

const buscarDadosPorCNPJ = async (cnpj) => {
    if (slcTipo.value !== 'pj') {
        return;
    }

    const cnpjLimpo = cnpj.replace(/\D/g, '');


    try {
        inpNome.value = "Buscando dados do CNPJ...";
        inpDataNasc.value = "";
        const apiResponse = await fetch(`https://publica.cnpj.ws/cnpj/${cnpjLimpo}`);

        if (!apiResponse.ok) {
            if (apiResponse.status === 404) {
                 throw new Error('CNPJ não encontrado');
            }
             if (apiResponse.status === 429) {
                 throw new Error('Tente novamente em breve');
            }
            throw new Error('Não foi possível consultar o CNPJ');
        }

        const dadosCnpj = await apiResponse.json();
        
        preencherCamposComDadosCnpj(dadosCnpj);

    } catch (error) {
        console.error(error);
        alert(error.message);
        inpNome.value = "";
    }
};

inpCpfCnpj.addEventListener('blur', (event) => {
    const cnpjinp = event.target.value;
    if (cnpjinp) {
        buscarDadosPorCNPJ(cnpjinp);
    }
});



const preencherCamposEndereco = (dadosEndereco) => {
    inpLogradouro.value = dadosEndereco.street || '';
    inpBairro.value = dadosEndereco.neighborhood || ''; 
    inpCidade.value = dadosEndereco.city || ''; 
    inpUf.value = dadosEndereco.state || ''; 
};

const buscarEnderecoPorCEP = async (cep) => {
    preencherCamposEndereco({}); 

    const cepLimpo = cep.replace(/\D/g, '');

    if (cepLimpo.length !== 8) {
        console.log("CEP inválido.");
        return;
    }

    try {
        inpLogradouro.value = "Buscando...";
        inpBairro.value = "...";
        inpCidade.value = "...";
        inpUf.value = "...";

        const apiResponse = await fetch(`https://brasilapi.com.br/api/cep/v1/${cepLimpo}`);
        
        if (!apiResponse.ok) {
            throw new Error('CEP não encontrado ou inválido.');
        }

        const dadosEndereco = await apiResponse.json();
        
        preencherCamposEndereco(dadosEndereco);
        inpNumero.focus();

    } catch (error) {
        console.error(error);
        alert(error.message);
        preencherCamposEndereco({});
    }
};

inpCep.addEventListener('blur', (event) => {
    const cepDigitado = event.target.value;
    if (cepDigitado) {
        buscarEnderecoPorCEP(cepDigitado);
    }
});





function salvarComoJson(dados, nomeArquivo = 'cadastro.json') {
    const jsonString = JSON.stringify(dados, null, 2);
    const blob = new Blob([jsonString], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = nomeArquivo; 
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    const submitButton = document.getElementById('submit');
    
    if (form && submitButton) {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            if (!form.checkValidity()) {
                form.reportValidity(); 
                return; 
            }

            const formData = new FormData(form);
            const dadosPlanos = Object.fromEntries(formData.entries());
            

            const dadosfim = {
    cpf_cnpj: dadosPlanos.cpf_cnpj,
    tipo: dadosPlanos.tipo,
    nome: dadosPlanos.nome,
    nome_fantasia: dadosPlanos.nome_fantasia || null,
    data_abertura_nascimento: dadosPlanos.data_abertura_nascimento || null,
    homepage: dadosPlanos.homepage || null,
    email: dadosPlanos.email,
    nome_contato: dadosPlanos.nome_contato || null,
    contato: dadosPlanos.contato,
    endereco: {
        cep: dadosPlanos.cep.replace(/\D/g, ''),
        logradouro: dadosPlanos.logradouro,
        numero: dadosPlanos.numero,
        complemento: dadosPlanos.complemento || null,
        bairro: dadosPlanos.bairro,
        cidade: dadosPlanos.cidade,
        uf: dadosPlanos.uf,
        pais: "Brasil"
    }
};

;
                        
            submitButton.disabled = true;
            submitButton.textContent = 'Finalizando cadastro...';

            try {
                const response = await fetch('api/clientes', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(dadosfim),
                    
                });
                console.log("dados enviados");

                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || `Erro ${response.status}`);
                }
                
                const resultado = await response.json();
                // Atualiza variável global
                alert('Cadastro realizado com sucesso!');
                form.reset();


            } catch (error) {
                console.error('Falha ao enviar o formulário:', error);
                alert(`Erro no cadastro: ${error.message}`);
            } finally {
                submitButton.disabled = false;
                submitButton.textContent = 'ENVIAR';
            }
        });
    } else {
        console.error('Formulário ou botão de submit não encontrado na página.');
    }
});


// ===================================================================
// === ADIÇÃO: Lista de Clientes (render / editar / excluir)        ===
// ===================================================================
(() => {
  // DOM refs da lista/modal
  const TBODY      = document.getElementById('tbody');
  const EMPTY_EL   = document.getElementById('empty');
  const SEARCH_INP = document.getElementById('search');
  const CLEAR_BTN  = document.getElementById('clearBtn');

  const DIALOG   = document.getElementById('editDialog');
  const FORM_MOD = document.getElementById('editForm');

  // Campos do modal
  const FM = {
    idHidden:      document.getElementById('f_id'),
    idDisplay:     document.getElementById('f_id_display'),
    cpf_cnpj:      document.getElementById('f_cpf_cnpj'),
    tipo:          document.getElementById('f_tipo'),
    nome:          document.getElementById('f_nome'),
    nome_fantasia: document.getElementById('f_nome_fantasia'),
    data_nasc:     document.getElementById('f_data_nasc'),
    homepage:      document.getElementById('f_homepage'),
    email:         document.getElementById('f_email'),
    nome_contato:  document.getElementById('f_nome_contato'),
    contato:       document.getElementById('f_contato'),
    cep:           document.getElementById('f_cep'),
    logradouro:    document.getElementById('f_logradouro'),
    numero:        document.getElementById('f_numero'),
    complemento:   document.getElementById('f_complemento'),
    bairro:        document.getElementById('f_bairro'),
    cidade:        document.getElementById('f_cidade'),
    pais:          document.getElementById('f_pais'),
    uf:            document.getElementById('f_uf'),
  };

  // Dados locais (exemplo). Depois você pode trocar por GET no backend.
  let clients = [
    {
      id: 1,
      codigo: '456',
      razao: 'LTDA',
      fantasia: 'LIMITADA',
      cnpj: '00.000.000/0001-45',
      // obrigatórios para o modal:
      cpf_cnpj: '00.000.000/0001-45',
      tipo: 'pj',
      nome: 'LTDA',
      email: 'contato@ltda.com.br',
      contato: '(11) 90000-0001',
      logradouro: 'Rua Exemplo',
      numero: '100',
      bairro: 'Centro',
      cidade: 'MORRINHOS - GO',
      uf: 'GO',
      pais: 'Brasil',
      // compat com tabela:
      telefone: '(11) 90000-0001'
    },
    {
      id: 2,
      codigo: '451',
      razao: 'Jumento',
      fantasia: 'Animal',
      cnpj: '62.95.000/0001-00',
      // obrigatórios:
      cpf_cnpj: '62.95.000/0001-00',
      tipo: 'pj',
      nome: 'Jorge',
      email: 'contato@jorge.com.br',
      contato: '(11) 90000-0002',
      logradouro: 'Av. das Américas',
      numero: '451',
      bairro: 'Jardim',
      cidade: 'ANGATUBA - SP',
      uf: 'SP',
      pais: 'Brasil',
      // compat:
      telefone: '(11) 90000-0002'
    },
    {
      id: 3,
      codigo: '2521',
      razao: 'CONCETO M',
      fantasia: 'CONCETO M',
      cnpj: '11.111.111/0001-11',
      // obrigatórios:
      cpf_cnpj: '11.111.111/0001-11',
      tipo: 'pj',
      nome: 'CONCETO M',
      email: 'comercial@concetom.com.br',
      contato: '(11) 90000-0003',
      logradouro: 'Rua das Flores',
      numero: '2521',
      bairro: 'Vila Nova',
      cidade: 'BRACO DO NORTE - SC',
      uf: 'SC',
      pais: 'Brasil',
      // compat:
      telefone: '(11) 90000-0003'
    }
  ];

  // Render da tabela
  function render(filter = '') {
    if (!TBODY) return;
    TBODY.innerHTML = '';
    const q = (filter || '').trim().toLowerCase();

    const filtered = clients.filter(c => {
      if (!q) return true;
      return [c.codigo, c.razao, c.fantasia, c.cnpj, c.cidade]
        .some(v => v && String(v).toLowerCase().includes(q));
    });

    if (EMPTY_EL) EMPTY_EL.hidden = filtered.length !== 0;

    for (const c of filtered) {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td><span class="mono">${escapeHtml(c.codigo || '')}</span></td>
        <td>${escapeHtml(c.razao || '')}</td>
        <td>${escapeHtml(c.fantasia || '')}</td>
        <td><span class="mono">${escapeHtml(c.cnpj || '')}</span></td>
        <td>${escapeHtml(c.cidade || '')}</td>
        <td>
          <div class="actions">
            <button type="button" class="table-btn edit">Editar</button>
            <button type="button" class="table-btn del">Excluir</button>
          </div>
        </td>
      `;

      tr.querySelector('.edit')?.addEventListener('click', () => openEdit(c.id));
      tr.querySelector('.del')?.addEventListener('click', () => delClient(c.id));

      TBODY.appendChild(tr);
    }
  }

  function escapeHtml(str) {
    if (!str) return '';
    return String(str).replaceAll('&', '&amp;')
                      .replaceAll('<', '&lt;')
                      .replaceAll('>', '&gt;')
                      .replaceAll('"', '&quot;')
                      .replaceAll("'", '&#39;');
  }

  // Busca / limpar
  CLEAR_BTN?.addEventListener('click', () => { if (SEARCH_INP) { SEARCH_INP.value = ''; } render(''); });
  SEARCH_INP?.addEventListener('input', (e) => render(e.target.value));

  // Abrir modal e preencher
  function openEdit(id) {
    const c = clients.find(x => x.id === id);
    if (!c) { alert('Cliente não encontrado.'); return; }

    FM.idHidden.value       = c.id;
    FM.idDisplay.value      = c.id;
    FM.cpf_cnpj.value       = c.cpf_cnpj ?? c.cnpj ?? '';
    FM.tipo.value           = c.tipo ?? (c.cnpj ? 'pj' : 'pf');
    FM.nome.value           = c.nome ?? c.razao ?? '';
    FM.nome_fantasia.value  = c.nome_fantasia ?? c.fantasia ?? '';
    FM.data_nasc.value      = c.data_abertura_nascimento ?? c.data ?? '';
    FM.homepage.value       = c.homepage ?? '';
    FM.email.value          = c.email ?? '';
    FM.nome_contato.value   = c.nome_contato ?? '';
    FM.contato.value        = c.contato ?? c.telefone ?? '';
    FM.cep.value            = c.cep ?? '';
    FM.logradouro.value     = c.logradouro ?? '';
    FM.numero.value         = c.numero ?? '';
    FM.complemento.value    = c.complemento ?? '';
    FM.bairro.value         = c.bairro ?? '';
    FM.cidade.value         = c.cidade ?? '';
    FM.pais.value           = c.pais ?? 'Brasil';
    FM.uf.value             = (c.uf ?? '').toString().toUpperCase();

    DIALOG?.showModal();
  }

  // Fechar modal
  document.getElementById('closeDialog')?.addEventListener('click', () => DIALOG?.close());
  document.getElementById('cancelBtn')?.addEventListener('click', (e) => { e.preventDefault(); DIALOG?.close(); });

  // Salvar edição
  FORM_MOD?.addEventListener('submit', (e) => {
    e.preventDefault();
    const id = Number(FM.idHidden.value || 0);
    if (!id) { alert('Edição sem ID não é permitida.'); return; }

    // Validação mínima (mantém seus required no HTML/CSS)
    const obrig = [
      [FM.cpf_cnpj, 'CPF/CNPJ'],
      [FM.tipo,     'Tipo'],
      [FM.nome,     'Nome'],
      [FM.email,    'Email'],
      [FM.contato,  'Contato'],
      [FM.logradouro, 'Logradouro'],
      [FM.numero,   'Número'],
      [FM.bairro,   'Bairro'],
      [FM.cidade,   'Cidade'],
      [FM.uf,       'UF'],
    ];
    for (const [el, label] of obrig) {
      if (!el || !el.value.trim()) { alert(`Preencha o campo obrigatório: ${label}.`); el?.focus(); return; }
    }

    const updated = {
      id,
      cpf_cnpj:  FM.cpf_cnpj.value.trim(),
      tipo:      FM.tipo.value,
      nome:      FM.nome.value.trim(),
      nome_fantasia: FM.nome_fantasia.value.trim(),
      data_abertura_nascimento: FM.data_nasc.value.trim(),
      homepage:  FM.homepage.value.trim(),
      email:     FM.email.value.trim(),
      nome_contato: FM.nome_contato.value.trim(),
      contato:   FM.contato.value.trim(),
      cep:       FM.cep.value.trim(),
      logradouro:FM.logradouro.value.trim(),
      numero:    FM.numero.value.trim(),
      complemento:FM.complemento.value.trim(),
      bairro:    FM.bairro.value.trim(),
      cidade:    FM.cidade.value.trim(),
      pais:      FM.pais.value.trim() || 'Brasil',
      uf:        FM.uf.value.trim().toUpperCase(),

      // compat com tabela
      razao:     FM.nome.value.trim(),
      fantasia:  FM.nome_fantasia.value.trim(),
      cnpj:      FM.cpf_cnpj.value.trim(),
      telefone:  FM.contato.value.trim(),
    };

    const idx = clients.findIndex(c => c.id === id);
    if (idx < 0) { alert('Cliente não encontrado.'); return; }
    clients[idx] = { ...clients[idx], ...updated };

    DIALOG?.close();
    render(SEARCH_INP?.value || '');
  });

  // Excluir
  function delClient(id) {
    const c = clients.find(x => x.id === id);
    if (!c) return;
    if (confirm(`Excluir o cliente ${c.razao || c.nome || ''} (código ${c.codigo || ''})?`)) {
      clients = clients.filter(x => x.id !== id);
      render(SEARCH_INP?.value || '');
    }
  }

  // Inicializa render ao carregar DOM
  document.addEventListener('DOMContentLoaded', () => render());
})();
