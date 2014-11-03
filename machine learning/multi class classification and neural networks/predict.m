function p = predict(Theta1, Theta2, X)
%PREDICT Predict the label of an input given a trained neural network
%   p = PREDICT(Theta1, Theta2, X) outputs the predicted label of X given the
%   trained weights of a neural network (Theta1, Theta2)

% Add bias unit to input (activation layer 1)
m = size(X, 1);
X = [ones(m, 1) X];

% Activation layer 2
a2 = X * Theta1';
z2 = sigmoid(a2);

% Add bias unit to activation layer 2
n = size(z2, 1);
z2 = [ones(n, 1) z2];

% Activation layer 3
a3 = z2 * Theta2';
z3 = sigmoid(a3);

% Prediction
[~, p] = max(z3, [], 2);

% =========================================================================


end
