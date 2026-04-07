# Core LLM Elements

* think of Language Models (LLMs) not as "databases" of facts, 
* but as massive statistical probability engines for sequences of tokens.

----


## 1. Tokens and Embeddings (The Data Layer)
* Computers cannot process words, only numbers.

* Tokenization: 
	* The process of breaking text into chunks (words, sub-words, or characters). 
	* For example, "helpful" might be split into help and ful.
* Embeddings: 
	* Each token is converted into a high-dimensional vector (a list of numbers). 
	* These vectors are positioned in a "***semantic space***" 
		* where related words (e.g., "king" and "queen") are *mathematically close to each other*.

## 2. The Transformer Architecture (The Engine)
* Modern LLMs (GPT, Llama, Claude) are built on the Transformer architecture. 
* Its core breakthrough is Self-Attention.

* Self-Attention: 
	* This mechanism allows the model to "weigh" the importance of every other word in a sentence 
		* when processing a specific word. 
	* In the sentence "The bank was closed because it was a holiday," 
		* attention helps the model understand that "it" refers to "the bank" and not "the holiday."
* Parallelization: 
	* Unlike older models (RNNs), 
	* Transformers can process entire sequences of text at once, 
	* making them much faster to train on massive datasets.

## 3. Parameters (The Memory)
* Parameters are the "weights" and "biases" inside the neural network.

* When we say a model has 70 Billion parameters, 
	* we are referring to 70B adjustable numerical values that were "fine-tuned" during training.
* These parameters represent the model's knowledge and 
	* its ability to predict the next token based on patterns learned from the internet/books.

## 4. Training Phases (The Learning Process)

* 1. Pre-training (Self-Supervised): 
	* The model reads trillions of words and plays a "guess the next word" game. 
	* This is where it learns grammar, facts, and reasoning, but it doesn't know how to follow instructions yet.
* 2. SFT (Supervised Fine-Tuning): 
	* The model is trained on high-quality examples of Q&A 
		* (e.g., "User: Write a poem. AI: [The Poem]"). 
		* This teaches it the "Assistant" format.
* 3. RLHF (Reinforcement Learning from Human Feedback): 
	* Humans rank model outputs (Good vs. Bad). 
	* This aligns the model to be helpful, honest, and harmless.

## 5. Context Window (The Working Memory)
* The Context Window is the maximum number of tokens the model can "keep in mind" at one time.

	* If a model has a 128k context window, 
		* it can "see" roughly 300 pages of text.
	* Once the conversation exceeds this limit, 
		* the model "forgets" the earliest parts of the chat.

## 6. Temperature and Top-P (The Control Knobs)
* These are inference parameters that control "creativity":

* Temperature: 
	* Controls randomness. 
	* A low temperature (0.1) makes the model predictable and factual. 
	* A high temperature (0.8+) makes it creative and diverse.
* Top-P (Nucleus Sampling): 
	* Limits the model to considering only the most likely pool of tokens whose cumulative probability reaches P (e.g., 0.9).

## Summary Table for Engineers

| Element | Analogy | Engineering Role |
|---|---|---|
| Token | Atomic Unit | Input/Output unit of measure |
| Embedding | Coordinate System | Mapping meaning to math |
| Attention | Dynamic Indexing | Contextual relationship mapping |
| Parameters | Config Files | The "weights" that define logic |
| Context Window | RAM / Buffer | Available memory for the current task |


