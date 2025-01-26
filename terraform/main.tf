# S3 Bucket para armazenar o código da Lambda
resource "aws_s3_bucket" "lambda_deploy" {
  bucket = var.s3_bucket_name
}

# Função Lambda
resource "aws_lambda_function" "api_lambda" {
  function_name = var.lambda_function_name
  role          = aws_iam_role.lambda_exec.arn
  handler       = "com.example.Handler::handleRequest"
  runtime       = "java11"

  s3_bucket        = aws_s3_bucket.lambda_deploy.id
  s3_key           = "lambda-function.jar"
  source_code_hash = filebase64sha256("../target/lambda-function.jar")

  environment {
    variables = {
      EXAMPLE_ENV = "value"
    }
  }
}

# IAM Role para a Lambda
resource "aws_iam_role" "lambda_exec" {
  name = "lambda_exec_role_${random_id.role_suffix.hex}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}

# Política de permissões para a Lambda
resource "aws_iam_role_policy" "lambda_policy" {
  name = "lambda_exec_policy"
  role = aws_iam_role.lambda_exec.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ]
        Resource = "arn:aws:logs:*:*:*"
      }
    ]
  })
}

# API Gateway
resource "aws_apigatewayv2_api" "http_api" {
  name          = var.api_gateway_name
  protocol_type = "HTTP"
}

# Integração Lambda com API Gateway
resource "aws_apigatewayv2_integration" "lambda_integration" {
  api_id             = aws_apigatewayv2_api.http_api.id
  integration_type   = "AWS_PROXY"
  integration_uri    = aws_lambda_function.api_lambda.invoke_arn
  payload_format_version = "2.0"
}

# Rota para a API
resource "aws_apigatewayv2_route" "default_route" {
  api_id    = aws_apigatewayv2_api.http_api.id
  route_key = "ANY /{proxy+}"

  target = "integrations/${aws_apigatewayv2_integration.lambda_integration.id}"
}

# Permissão para o API Gateway invocar a Lambda
resource "aws_lambda_permission" "apigateway_permission" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.api_lambda.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_apigatewayv2_api.http_api.execution_arn}/*"
}

# Output da URL da API
output "api_url" {
  value = aws_apigatewayv2_api.http_api.api_endpoint
}
