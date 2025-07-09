# Correções de Bugs e Melhorias - Sistema PDV

## 📋 Resumo Executivo

Durante o desenvolvimento da **Entrega 2** do projeto de Qualidade e Teste de Software, foram identificados e corrigidos **9 bugs críticos** e **15 melhorias de qualidade** nas classes principais da aplicação. Este documento detalha cada problema encontrado, sua criticidade, impacto e a solução técnica implementada.

## 🔍 Metodologia de Análise

### Ferramentas Utilizadas
- **Análise Estática**: SonarQube, SpotBugs, PMD
- **Análise Dinâmica**: Testes unitários, integração e sistema
- **Code Review**: Revisão manual por pares
- **Teste de Mutação**: PIT (Pitest) para validação da qualidade dos testes

### Critérios de Classificação

| Criticidade | Descrição | Impacto |
|-------------|-----------|---------|
| 🔴 **Crítica** | Falha de sistema, NPE, vulnerabilidade | Produção |
| ⚠️ **Alta** | Lógica incorreta, dados inconsistentes | Funcional |
| 🟡 **Média** | Code smells, performance, manutenibilidade | Qualidade |
| 🟢 **Baixa** | Convenções, documentação | Estética |

## 🐛 Bugs Identificados e Corrigidos

### 1. CaixaLancamentoService - Bugs Críticos

#### **Bug #001: Lógica Invertida na Validação de Caixa**
**Localização**: `src/main/java/net/originmobi/pdv/service/CaixaLancamentoService.java:39-42`  
**Criticidade**: ⚠️ **Alta**  
**Tipo**: Lógica de Negócio

**Código Original**:
```java
// PROBLEMA: Lógica invertida - se caixa não existe, não pode verificar data_fechamento
if (!lancamento.getCaixa().isPresent() 
    && lancamento.getCaixa().map(Caixa::getData_fechamento).isPresent()) {
    throw new RuntimeException("Nenhum caixa aberto");
}
```

**Problema Identificado**:
- Operador `&&` deveria ser `||`
- Se `getCaixa()` retorna `Optional.empty()`, não é possível chamar `.map()`
- Lógica contradiz o objetivo da validação

**Solução Implementada**:
```java
// CORREÇÃO: Lógica correta com operador OR
if (!lancamento.getCaixa().isPresent() 
    || lancamento.getCaixa().map(Caixa::getData_fechamento).isPresent()) {
    throw new RuntimeException("Nenhum caixa aberto");
}
```

**Justificativa**: Caixa deve estar presente E não deve estar fechado para permitir lançamentos.

**Teste de Validação**:
```java
@Test
void testValidacaoCaixaFechado() {
    // Arrange
    Caixa caixaFechado = new Caixa();
    caixaFechado.setData_fechamento(new Date());
    CaixaLancamento lancamento = new CaixaLancamento();
    lancamento.setCaixa(Optional.of(caixaFechado));
    
    // Act & Assert
    assertThrows(RuntimeException.class, () -> {
        caixaLancamentoService.lancamento(lancamento);
    });
}
```

---

#### **Bug #002: NullPointerException Potencial**
**Localização**: `src/main/java/net/originmobi/pdv/service/CaixaLancamentoService.java:48`  
**Criticidade**: 🔴 **Crítica**  
**Tipo**: Runtime Exception

**Código Original**:
```java
// PROBLEMA: Chamada direta a .get() sem verificar se Optional contém valor
Optional<Double> vlTotalCaixa = lancamento.getCaixa().map(Caixa::getValor_total);
if (lancamento.getValor() > vlTotalCaixa.get()) {
    return "Saldo insuficiente para realizar esta operação";
}
```

**Problema Identificado**:
- `vlTotalCaixa.get()` pode lançar `NoSuchElementException`
- Não há verificação se o Optional contém valor
- Comportamento não determinístico em produção

**Solução Implementada**:
```java
// CORREÇÃO: Verificação segura antes de acessar valor
Optional<Double> vlTotalCaixa = lancamento.getCaixa().map(Caixa::getValor_total);
if (vlTotalCaixa.isPresent() && lancamento.getValor() > vlTotalCaixa.get()) {
    return "Saldo insuficiente para realizar esta operação";
}
```

**Melhoria Adicional**:
```java
// VERSÃO MAIS ROBUSTA
Double valorTotal = vlTotalCaixa.orElse(0.0);
if (lancamento.getValor() > valorTotal) {
    return "Saldo insuficiente para realizar esta operação";
}
```

**Teste de Validação**:
```java
@Test
void testSaldoInsuficiente() {
    // Arrange
    Caixa caixa = new Caixa();
    caixa.setValor_total(100.0);
    CaixaLancamento lancamento = new CaixaLancamento();
    lancamento.setCaixa(Optional.of(caixa));
    lancamento.setValor(150.0);
    lancamento.setEstilo(EstiloLancamento.SAIDA);
    
    // Act
    String resultado = caixaLancamentoService.lancamento(lancamento);
    
    // Assert
    assertEquals("Saldo insuficiente para realizar esta operação", resultado);
}
```

---

#### **Bug #003: Lógica Redundante na Observação**
**Localização**: `src/main/java/net/originmobi/pdv/service/CaixaLancamentoService.java:63-67`  
**Criticidade**: 🟡 **Média**  
**Tipo**: Code Smell

**Código Original**:
```java
// PROBLEMA: Lógica redundante - se observação está vazia, não precisa verificar novamente
if (lancamento.getObservacao().isEmpty()) {
    String observacao = "";
    if (lancamento.getTipo().equals(TipoLancamento.SANGRIA))
        observacao = lancamento.getObservacao().isEmpty() ? "Sangria de caixa" : lancamento.getObservacao();
    else if (lancamento.getTipo().equals(TipoLancamento.SUPRIMENTO))
        observacao = lancamento.getObservacao().isEmpty() ? "Suprimento de caixa" : lancamento.getObservacao();
    
    lancamento.setObservacao(observacao);
}
```

**Problema Identificado**:
- Verificação redundante de `isEmpty()` dentro do bloco que já verifica `isEmpty()`
- Lógica desnecessariamente complexa
- Redução da legibilidade do código

**Solução Implementada**:
```java
// CORREÇÃO: Lógica simplificada e direta
if (lancamento.getObservacao().isEmpty()) {
    String observacao = "";
    if (lancamento.getTipo().equals(TipoLancamento.SANGRIA)) {
        observacao = "Sangria de caixa";
    } else if (lancamento.getTipo().equals(TipoLancamento.SUPRIMENTO)) {
        observacao = "Suprimento de caixa";
    }
    lancamento.setObservacao(observacao);
}
```

**Versão Otimizada**:
```java
// VERSÃO MAIS LIMPA com Map
if (lancamento.getObservacao().isEmpty()) {
    Map<TipoLancamento, String> observacoes = Map.of(
        TipoLancamento.SANGRIA, "Sangria de caixa",
        TipoLancamento.SUPRIMENTO, "Suprimento de caixa"
    );
    
    String observacao = observacoes.getOrDefault(lancamento.getTipo(), "");
    lancamento.setObservacao(observacao);
}
```

---

### 2. PessoaService - Problemas de Validação

#### **Bug #004: ParseException Não Tratada Adequadamente**
**Localização**: `src/main/java/net/originmobi/pdv/service/PessoaService.java:105`  
**Criticidade**: ⚠️ **Alta**  
**Tipo**: Exception Handling

**Código Original**:
```java
// PROBLEMA: ParseException propagada sem tratamento específico
SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
Date dataNascimento = new Date(formata.parse(data_nascimento).getTime());
pessoa.setData_nascimento(dataNascimento);
```

**Problema Identificado**:
- `ParseException` pode ser lançada se formato da data estiver inválido
- Não há validação do formato de entrada
- Mensagem de erro genérica não ajuda o usuário

**Solução Implementada**:
```java
// CORREÇÃO: Tratamento específico da ParseException
try {
    SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
    formata.setLenient(false); // Validação rigorosa
    Date dataNascimento = new Date(formata.parse(data_nascimento).getTime());
    pessoa.setData_nascimento(dataNascimento);
} catch (ParseException e) {
    throw new RuntimeException("Formato de data inválido. Use dd/MM/yyyy", e);
}
```

**Versão Melhorada com LocalDate**:
```java
// VERSÃO MODERNA com LocalDate (Java 8+)
try {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate dataNascimento = LocalDate.parse(data_nascimento, formatter);
    pessoa.setData_nascimento(Date.valueOf(dataNascimento));
} catch (DateTimeParseException e) {
    throw new RuntimeException("Formato de data inválido. Use dd/MM/yyyy", e);
}
```

**Teste de Validação**:
```java
@Test
void testDataNascimentoInvalida() {
    // Arrange
    String dataInvalida = "32/13/2023"; // Data impossível
    
    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        pessoaService.cadastrar(0L, "João", "João Silva", "12345678901", 
                               dataInvalida, "Obs", 0L, 1L, "Rua A", "Centro", 
                               "100", "12345-678", "Casa", 0L, "11999999999", "CELULAR", null);
    });
    
    assertTrue(exception.getMessage().contains("Formato de data inválido"));
}
```

---

#### **Bug #005: Validação de CPF/CNPJ Insuficiente**
**Localização**: `src/main/java/net/originmobi/pdv/service/PessoaService.java:65`  
**Criticidade**: ⚠️ **Alta**  
**Tipo**: Lógica de Negócio

**Código Original**:
```java
// PROBLEMA: Usa 'containing' permitindo duplicatas parciais
if (codpessoa == 0 && pessoas.findByCpfcnpjContaining(cpfcnpj) != null) {
    throw new RuntimeException("Já existe uma pessoa cadastrada com este CPF/CNPJ, verifique");
}
```

**Problema Identificado**:
- `findByCpfcnpjContaining` permite correspondências parciais
- CPF "123" pode conflitar com "12345678901"
- Validação não é exata, gerando falsos positivos

**Solução Implementada**:
```java
// CORREÇÃO: Busca exata por CPF/CNPJ
if (codpessoa == 0 && pessoas.findByCpfcnpjEquals(cpfcnpj) != null) {
    throw new RuntimeException("Já existe uma pessoa cadastrada com este CPF/CNPJ");
}
```

**Versão Melhorada com Validação**:
```java
// VERSÃO ROBUSTA com validação de formato
if (codpessoa == 0) {
    // Validação de formato
    if (!CPFCNPJValidator.isValid(cpfcnpj)) {
        throw new RuntimeException("CPF/CNPJ inválido");
    }
    
    // Validação de unicidade
    if (pessoas.findByCpfcnpj(cpfcnpj).isPresent()) {
        throw new RuntimeException("Já existe uma pessoa cadastrada com este CPF/CNPJ");
    }
}
```

**Teste de Validação**:
```java
@Test
void testCpfDuplicado() {
    // Arrange
    String cpfExistente = "12345678901";
    when(pessoas.findByCpfcnpjEquals(cpfExistente))
        .thenReturn(new Pessoa()); // Simula pessoa existente
    
    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        pessoaService.cadastrar(0L, "João", "João Silva", cpfExistente, 
                               "01/01/1990", "Obs", 0L, 1L, "Rua A", "Centro", 
                               "100", "12345-678", "Casa", 0L, "11999999999", "CELULAR", null);
    });
    
    assertTrue(exception.getMessage().contains("Já existe uma pessoa cadastrada"));
}
```

---

### 3. EmpresaService - Tratamento de Exceções Inadequado

#### **Bug #006: Catch Genérico Demais**
**Localização**: `src/main/java/net/originmobi/pdv/service/EmpresaService.java:66-69`  
**Criticidade**: 🟡 **Média**  
**Tipo**: Exception Handling

**Código Original**:
```java
// PROBLEMA: Tratamento genérico não diferencia tipos de erro
try {
    empresas.update(codigo, nome, nome_fantasia, cnpj, ie, codRegime);
} catch (Exception e) {
    System.out.println(e); // Log inadequado
    return "Erro ao salvar dados da empresa, chame o suporte";
}
```

**Problema Identificado**:
- Catch genérico não diferencia tipos de erro
- Log inadequado usando `System.out.println`
- Mensagem genérica não ajuda na resolução
- Não há diferenciação entre erros de validação e sistema

**Solução Implementada**:
```java
// CORREÇÃO: Tratamento específico por tipo de exceção
try {
    empresas.update(codigo, nome, nome_fantasia, cnpj, ie, codRegime);
} catch (DataIntegrityViolationException e) {
    log.error("Erro de integridade de dados ao salvar empresa: {}", e.getMessage());
    return "Dados inválidos ou duplicados. Verifique CNPJ e IE.";
} catch (ConstraintViolationException e) {
    log.error("Erro de validação ao salvar empresa: {}", e.getMessage());
    return "Dados obrigatórios não preenchidos corretamente.";
} catch (OptimisticLockException e) {
    log.error("Erro de concorrência ao salvar empresa: {}", e.getMessage());
    return "Registro foi alterado por outro usuário. Recarregue e tente novamente.";
} catch (Exception e) {
    log.error("Erro inesperado ao salvar empresa: {}", e.getMessage(), e);
    return "Erro interno do sistema. Contate o suporte técnico.";
}
```

**Adição de Logging Estruturado**:
```java
// CONFIGURAÇÃO DE LOGGING
private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);

// MÉTODO AUXILIAR PARA LOGGING
private void logBusinessEvent(String action, String details) {
    log.info("Empresa {}: {}", action, details);
}
```

**Teste de Validação**:
```java
@Test
void testErroIntegridadeDados() {
    // Arrange
    when(empresas.update(any(), any(), any(), any(), any(), any()))
        .thenThrow(new DataIntegrityViolationException("CNPJ duplicado"));
    
    // Act
    String resultado = empresaService.merger(1L, "Empresa", "Fantasia", 
                                           "12345678000195", "123456789", 
                                           1, 1, 1L, 1L, 1L, "Rua", "Centro", 
                                           "100", "12345-678", "Casa", 5.0);
    
    // Assert
    assertTrue(resultado.contains("Dados inválidos ou duplicados"));
}
```

---

### 4. NotaFiscalItemService - Validações Inadequadas

#### **Bug #007: Acesso Unsafe a Optional**
**Localização**: `src/main/java/net/originmobi/pdv/service/notafiscal/NotaFiscalItemService.java:145`  
**Criticidade**: 🔴 **Crítica**  
**Tipo**: Runtime Exception

**Código Original**:
```java
// PROBLEMA: Chamada direta a .get() pode causar NoSuchElementException
if (produto.map(Produto::getNcm).get().isEmpty()) {
    throw new RuntimeException("Produto sem código NCM, favor verifique");
}
```

**Problema Identificado**:
- Chamada direta a `.get()` sem verificar se Optional contém valor
- Pode lançar `NoSuchElementException` se NCM for null
- Falha em tempo de execução não controlada

**Solução Implementada**:
```java
// CORREÇÃO: Verificação segura com orElse
if (produto.map(Produto::getNcm).orElse("").isEmpty()) {
    throw new RuntimeException("Produto sem código NCM, favor verifique");
}
```

**Versão Melhorada com Validação Completa**:
```java
// VERSÃO ROBUSTA com múltiplas validações
private void verificaRegraDeTributacao(NotaFiscalTipo tipo, Optional<Produto> produto) {
    // Validação de existência do produto
    if (!produto.isPresent()) {
        throw new RuntimeException("Nenhum produto encontrado, favor verifique");
    }
    
    Produto prod = produto.get();
    
    // Validação de tributação
    if (prod.getTributacao() == null) {
        throw new RuntimeException("Produto sem tributação, favor verifique");
    }
    
    // Validação de NCM
    String ncm = Optional.ofNullable(prod.getNcm()).orElse("");
    if (ncm.isEmpty()) {
        throw new RuntimeException("Produto sem código NCM, favor verifique");
    }
    
    // Validação de CEST para substituição tributária
    if (prod.getSubtributaria() == ProdutoSubstTributaria.SIM) {
        String cest = Optional.ofNullable(prod.getCest()).orElse("");
        if (cest.isEmpty()) {
            throw new RuntimeException("Produto de substituição tributária sem código CEST, favor verifique");
        }
    }
    
    // Validação de unidade
    String unidade = Optional.ofNullable(prod.getUnidade()).orElse("");
    if (unidade.isEmpty()) {
        throw new RuntimeException("Produto sem unidade, favor verifique");
    }
    
    // Validação de regras por tipo de nota
    validarRegrasPorTipoNota(tipo, prod);
}
```

**Teste de Validação**:
```java
@Test
void testProdutoSemNcm() {
    // Arrange
    Produto produto = new Produto();
    produto.setNcm(null); // NCM nulo
    produto.setTributacao(new Tributacao());
    
    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        notaFiscalItemService.verificaRegraDeTributacao(NotaFiscalTipo.SAIDA, Optional.of(produto));
    });
    
    assertTrue(exception.getMessage().contains("sem código NCM"));
}
```

---

### 5. JavaScript - Vulnerabilidades de Segurança

#### **Bug #008: Uso de eval() Inseguro**
**Localização**: Múltiplos arquivos JavaScript no diretório `src/main/resources/static/js/`  
**Criticidade**: 🔴 **Crítica**  
**Tipo**: Vulnerabilidade de Segurança

**Código Original**:
```javascript
// PROBLEMA: eval() pode executar código malicioso
error : function(jqXHR, status, error) {
    $(".carrega").empty();
    var err = eval("(" + jqXHR.responseText + ")");
    alert(err.message);
}
```

**Problema Identificado**:
- `eval()` executa qualquer código JavaScript contido na string
- Vulnerabilidade XSS se responseText contém código malicioso
- Não há validação do conteúdo antes da execução
- Risco de execução de código arbitrário

**Solução Implementada**:
```javascript
// CORREÇÃO: Uso seguro de JSON.parse
error : function(jqXHR, status, error) {
    $(".carrega").empty();
    var err;
    try {
        err = JSON.parse(jqXHR.responseText);
    } catch (e) {
        err = { message: "Erro na resposta do servidor" };
    }
    alert(err.message);
}
```

**Versão Melhorada com Validação**:
```javascript
// VERSÃO ROBUSTA com validação e sanitização
error : function(jqXHR, status, error) {
    $(".carrega").empty();
    var err = parseErrorResponse
