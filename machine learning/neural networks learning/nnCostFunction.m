function [J, grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% -------------------------
% Begin Forward Propogation

% Activation layer 1 (input)
a1 = X;

% Add bias unit to activation layer 2
a1 = [ones(size(a1, 1), 1) a1];

% Compute activation layer 2 (hidden layer)
z2 = a1 * Theta1';
a2 = sigmoid(z2);

% Add bias unit to activation layer 2
a2 = [ones(size(a2, 1), 1) a2];

% Compute activation layer 3 (output)
z3 = a2 * Theta2';
a3 = sigmoid(z3);

% Assign activation layer 3 to output
output = a3;

% Re-init Y for multi class classification
temp = eye(num_labels);
y = temp(y,:);

% Use y to determine J (cost function)
J = (1/m) * sum(sum( (-y .* log(output)) - ((1 - y) .* log(1-output))));

% Generate theta1 and theta2 without bias unit (for regularization)
Theta1_regularization = Theta1(:, 2:end);
Theta2_regularization = Theta2(:, 2:end);

% Compute regularization term
Regulaiztion_term = (lambda / (2 * m ) ) ...
    * (sum(sum(Theta1_regularization .^ 2)) + sum(sum(Theta2_regularization .^ 2)));


% Add regularization to cost function
J = J + Regulaiztion_term;

% End Forward Propogation
% -----------------------

% --------------------------
% Begin Backward Propogation

% Delta3 = predicted output - actual output
% Delta3 = output - y
d3 = output - y;

% Delta2 = (Theta2)'*(Delta3) .* g'(z2)
% Afeter computing Delta2, remove bias unit
d2 = (d3 * Theta2) .* [ones(size(z2, 1), 1) sigmoidGradient(z2)];
d2 = d2(:, 2:end);

% Compute gradients
Theta2_grad = (1/m) * d3' * a2;
Theta1_grad = (1/m) * d2' * a1;

% Add regularization to gradients
Theta2_grad = Theta2_grad + ((lambda / m) * [zeros(size(Theta2, 1), 1) Theta2(:, 2:end)]);
Theta1_grad = Theta1_grad + ((lambda / m) * [zeros(size(Theta1, 1), 1) Theta1(:, 2:end)]);

% End Backward Propogation
% ------------------------

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
