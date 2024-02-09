install:
    # Cria o diretório .git/hooks se ainda não existir
	mkdir -p .git/hooks
	
    # Script do pre-commit
	echo '#!/bin/bash' > .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Caminho para o executável do lint' >> .git/hooks/pre-commit
	echo 'LINT_COMMAND="./gradlew lint"' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Caminho para o executável do build' >> .git/hooks/pre-commit
	echo 'BUILD_COMMAND="./gradlew build"' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Executa o lint' >> .git/hooks/pre-commit
	echo '$$LINT_COMMAND' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Armazena o status de saída do lint' >> .git/hooks/pre-commit
	echo 'LINT_RESULT=$$?' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Verifica se houve erros no lint' >> .git/hooks/pre-commit
	echo 'if [ $$LINT_RESULT -ne 0 ]; then' >> .git/hooks/pre-commit
	echo '  echo "Erro: O lint encontrou problemas no código. Corrija-os antes de fazer o commit."' >> .git/hooks/pre-commit
	echo '  exit 1' >> .git/hooks/pre-commit
	echo 'fi' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Executa o build' >> .git/hooks/pre-commit
	echo '$$BUILD_COMMAND' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Armazena o status de saída do build' >> .git/hooks/pre-commit
	echo 'BUILD_RESULT=$$?' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Verifica se houve erros no build' >> .git/hooks/pre-commit
	echo 'if [ $$BUILD_RESULT -ne 0 ]; then' >> .git/hooks/pre-commit
	echo '  echo "Erro: O build encontrou problemas. Corrija-os antes de fazer o commit."' >> .git/hooks/pre-commit
	echo '  exit 1' >> .git/hooks/pre-commit
	echo 'fi' >> .git/hooks/pre-commit
	echo '' >> .git/hooks/pre-commit
	echo '# Se não houve erros no lint ou no build, o commit pode ser feito' >> .git/hooks/pre-commit
	echo 'echo "Commit permitido."' >> .git/hooks/pre-commit
	echo 'exit 0' >> .git/hooks/pre-commit

    # Tornar o hook executável
	chmod +x .git/hooks/pre-commit

    # Mensagem de sucesso
	@echo "\033[0;32mHooks de pre-commit instalados com sucesso!\033[0m" 

	@(cd docs && sudo npm install) > /dev/null 2>&1

	@echo "\033[0;32mDependências instaladas com sucesso no diretório 'docs'!\033[0m"